using System.Diagnostics;
using System.Net.Http.Json;
using System.Text;

namespace AerospaceHealthcareControlPanel;

public sealed class MainForm : Form
{
    private readonly HttpClient httpClient = new();
    private readonly System.Windows.Forms.Timer refreshTimer = new() { Interval = 3000 };
    private readonly TextBox backendUrlTextBox = new() { Text = "http://localhost:8080", Dock = DockStyle.Fill };
    private readonly TextBox analysisUrlTextBox = new() { Text = "http://localhost:8000", Dock = DockStyle.Fill };
    private readonly TextBox simulatorPathTextBox = new() { Text = @"..\cpp-simulator\build\sensor-simulator.exe", Dock = DockStyle.Fill };
    private readonly TextBox latestValuesTextBox = new() { Multiline = true, ReadOnly = true, ScrollBars = ScrollBars.Vertical, Dock = DockStyle.Fill };
    private readonly TextBox alertsTextBox = new() { Multiline = true, ReadOnly = true, ScrollBars = ScrollBars.Vertical, Dock = DockStyle.Fill };
    private Process? simulatorProcess;

    public MainForm()
    {
        Text = "Aerospace and Healthcare Monitoring Control Panel";
        Width = 1100;
        Height = 700;

        var startButton = new Button { Text = "Start Monitoring", Dock = DockStyle.Fill };
        var stopButton = new Button { Text = "Stop Monitoring", Dock = DockStyle.Fill };
        var refreshButton = new Button { Text = "Refresh", Dock = DockStyle.Fill };
        var exportButton = new Button { Text = "Export Report", Dock = DockStyle.Fill };

        startButton.Click += async (_, _) => await StartMonitoringAsync();
        stopButton.Click += (_, _) => StopMonitoring();
        refreshButton.Click += async (_, _) => await RefreshDashboardAsync();
        exportButton.Click += async (_, _) => await ExportReportAsync();
        refreshTimer.Tick += async (_, _) => await RefreshDashboardAsync();

        var layout = new TableLayoutPanel
        {
            Dock = DockStyle.Fill,
            ColumnCount = 2,
            RowCount = 6,
            Padding = new Padding(12)
        };

        layout.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 50));
        layout.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 50));
        for (var index = 0; index < 4; index++)
        {
            layout.RowStyles.Add(new RowStyle(SizeType.AutoSize));
        }

        layout.RowStyles.Add(new RowStyle(SizeType.Percent, 50));
        layout.RowStyles.Add(new RowStyle(SizeType.Percent, 50));

        layout.Controls.Add(new Label { Text = "Backend API URL", AutoSize = true }, 0, 0);
        layout.Controls.Add(backendUrlTextBox, 1, 0);
        layout.Controls.Add(new Label { Text = "Python Analysis URL", AutoSize = true }, 0, 1);
        layout.Controls.Add(analysisUrlTextBox, 1, 1);
        layout.Controls.Add(new Label { Text = "Simulator Executable Path", AutoSize = true }, 0, 2);
        layout.Controls.Add(simulatorPathTextBox, 1, 2);

        var buttonRow = new TableLayoutPanel { Dock = DockStyle.Fill, ColumnCount = 4, AutoSize = true };
        for (var index = 0; index < 4; index++)
        {
            buttonRow.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 25));
        }

        buttonRow.Controls.Add(startButton, 0, 0);
        buttonRow.Controls.Add(stopButton, 1, 0);
        buttonRow.Controls.Add(refreshButton, 2, 0);
        buttonRow.Controls.Add(exportButton, 3, 0);

        layout.Controls.Add(buttonRow, 0, 3);
        layout.SetColumnSpan(buttonRow, 2);
        layout.Controls.Add(new GroupBox { Text = "Latest Sensor Values", Dock = DockStyle.Fill, Controls = { latestValuesTextBox } }, 0, 4);
        layout.Controls.Add(new GroupBox { Text = "Alerts", Dock = DockStyle.Fill, Controls = { alertsTextBox } }, 1, 4);
        layout.Controls.Add(new GroupBox { Text = "Control Panel Notes", Dock = DockStyle.Fill, Controls = { BuildNotesBox() } }, 0, 5);
        layout.SetColumnSpan(layout.GetControlFromPosition(0, 5)!, 2);

        Controls.Add(layout);
    }

    private static Control BuildNotesBox()
    {
        return new TextBox
        {
            Multiline = true,
            ReadOnly = true,
            Dock = DockStyle.Fill,
            Text = "1. Start the Spring Boot backend.\r\n2. Start the Python analysis API.\r\n3. Point the simulator path to the compiled C++ executable.\r\n4. Click Start Monitoring, then Export Report when alerts appear."
        };
    }

    private async Task StartMonitoringAsync()
    {
        if (simulatorProcess is { HasExited: false })
        {
            await RefreshDashboardAsync();
            return;
        }

        var simulatorPath = simulatorPathTextBox.Text.Trim();
        if (!File.Exists(simulatorPath))
        {
            MessageBox.Show($"Simulator not found: {simulatorPath}", "Missing executable", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            return;
        }

        simulatorProcess = Process.Start(new ProcessStartInfo
        {
            FileName = simulatorPath,
            WorkingDirectory = Path.GetDirectoryName(simulatorPath),
            UseShellExecute = false
        });

        refreshTimer.Start();
        await RefreshDashboardAsync();
    }

    private void StopMonitoring()
    {
        refreshTimer.Stop();
        if (simulatorProcess is { HasExited: false })
        {
            simulatorProcess.Kill(true);
            simulatorProcess.Dispose();
            simulatorProcess = null;
        }
    }

    private async Task RefreshDashboardAsync()
    {
        try
        {
            var latest = await httpClient.GetFromJsonAsync<SensorRecord>($"{backendUrlTextBox.Text.TrimEnd('/')}/api/sensors/latest");
            var alerts = await httpClient.GetFromJsonAsync<List<AlertRecord>>($"{backendUrlTextBox.Text.TrimEnd('/')}/api/alerts");

            latestValuesTextBox.Text = latest is null
                ? "No sensor records available yet."
                : $"Timestamp: {latest.Timestamp}\r\nTemperature: {latest.Temperature}\r\nPressure: {latest.Pressure}\r\nVibration: {latest.Vibration}\r\nOxygen Level: {latest.OxygenLevel}\r\nHeart Rate: {latest.HeartRate}\r\nBattery Level: {latest.BatteryLevel}\r\nChecksum: {latest.Checksum}";

            alertsTextBox.Text = alerts is null || alerts.Count == 0
                ? "No alerts recorded."
                : string.Join(Environment.NewLine, alerts
                    .OrderByDescending(alert => alert.CreatedAt)
                    .Take(10)
                    .Select(alert => $"[{alert.Severity}] {alert.AlertType} at {alert.CreatedAt}: {alert.Message}"));
        }
        catch (Exception exception)
        {
            alertsTextBox.Text = $"Unable to load dashboard data.\r\n{exception.Message}";
        }
    }

    private async Task ExportReportAsync()
    {
        try
        {
            var report = await httpClient.GetStringAsync($"{analysisUrlTextBox.Text.TrimEnd('/')}/api/analysis/report");
            var desktopPath = Environment.GetFolderPath(Environment.SpecialFolder.DesktopDirectory);
            var reportPath = Path.Combine(desktopPath, $"monitoring-report-{DateTime.Now:yyyyMMdd-HHmmss}.txt");
            await File.WriteAllTextAsync(reportPath, report, Encoding.UTF8);
            MessageBox.Show($"Report exported to {reportPath}", "Report saved", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
        catch (Exception exception)
        {
            MessageBox.Show(exception.Message, "Export failed", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    protected override void OnFormClosed(FormClosedEventArgs e)
    {
        StopMonitoring();
        httpClient.Dispose();
        base.OnFormClosed(e);
    }
}

public sealed class SensorRecord
{
    public long Id { get; set; }
    public double Temperature { get; set; }
    public double Pressure { get; set; }
    public double Vibration { get; set; }
    public double OxygenLevel { get; set; }
    public int HeartRate { get; set; }
    public double BatteryLevel { get; set; }
    public string Timestamp { get; set; } = string.Empty;
    public int Checksum { get; set; }
}

public sealed class AlertRecord
{
    public long Id { get; set; }
    public string AlertType { get; set; } = string.Empty;
    public string Message { get; set; } = string.Empty;
    public string Severity { get; set; } = string.Empty;
    public string CreatedAt { get; set; } = string.Empty;
}