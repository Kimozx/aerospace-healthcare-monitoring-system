library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity alert_circuit_tb is
end alert_circuit_tb;

architecture simulation of alert_circuit_tb is
    signal temperature_high : STD_LOGIC := '0';
    signal pressure_high    : STD_LOGIC := '0';
    signal oxygen_low       : STD_LOGIC := '0';
    signal battery_low      : STD_LOGIC := '0';
    signal alert_signal     : STD_LOGIC;
begin
    uut: entity work.alert_circuit
        port map (
            temperature_high => temperature_high,
            pressure_high => pressure_high,
            oxygen_low => oxygen_low,
            battery_low => battery_low,
            alert_signal => alert_signal
        );

    stimulus: process
    begin
        wait for 10 ns;
        temperature_high <= '1';
        wait for 10 ns;
        temperature_high <= '0';
        pressure_high <= '1';
        wait for 10 ns;
        pressure_high <= '0';
        oxygen_low <= '1';
        wait for 10 ns;
        oxygen_low <= '0';
        battery_low <= '1';
        wait for 10 ns;
        battery_low <= '0';
        wait;
    end process;
end simulation;library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity alert_circuit_tb is
end alert_circuit_tb;

architecture behavior of alert_circuit_tb is
    signal temperature_high : STD_LOGIC := '0';
    signal pressure_high    : STD_LOGIC := '0';
    signal oxygen_low       : STD_LOGIC := '0';
    signal battery_low      : STD_LOGIC := '0';
    signal alert_signal     : STD_LOGIC;
begin
    uut: entity work.alert_circuit
        port map (
            temperature_high => temperature_high,
            pressure_high => pressure_high,
            oxygen_low => oxygen_low,
            battery_low => battery_low,
            alert_signal => alert_signal
        );

    stimulus: process
    begin
        wait for 10 ns;
        temperature_high <= '1';
        wait for 10 ns;
        temperature_high <= '0';
        oxygen_low <= '1';
        wait for 10 ns;
        oxygen_low <= '0';
        battery_low <= '1';
        wait for 10 ns;
        battery_low <= '0';
        pressure_high <= '1';
        wait;
    end process;
end behavior;