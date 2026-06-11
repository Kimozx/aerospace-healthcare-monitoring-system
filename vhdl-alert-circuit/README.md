# VHDL Alert Circuit

This folder contains a simple hardware-oriented alert circuit model.

Inputs:

1. `temperature_high`
2. `pressure_high`
3. `oxygen_low`
4. `battery_low`

Output:

1. `alert_signal`

Behavior:

If any input is active, `alert_signal` becomes active.