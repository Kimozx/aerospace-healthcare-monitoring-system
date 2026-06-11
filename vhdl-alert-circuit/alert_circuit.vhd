library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity alert_circuit is
    Port (
        temperature_high : in STD_LOGIC;
        pressure_high    : in STD_LOGIC;
        oxygen_low       : in STD_LOGIC;
        battery_low      : in STD_LOGIC;
        alert_signal     : out STD_LOGIC
    );
end alert_circuit;

architecture Behavioral of alert_circuit is
begin
    alert_signal <= temperature_high or pressure_high or oxygen_low or battery_low;
end Behavioral;library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity alert_circuit is
    Port (
        temperature_high : in STD_LOGIC;
        pressure_high    : in STD_LOGIC;
        oxygen_low       : in STD_LOGIC;
        battery_low      : in STD_LOGIC;
        alert_signal     : out STD_LOGIC
    );
end alert_circuit;

architecture Behavioral of alert_circuit is
begin
    alert_signal <= temperature_high or pressure_high or oxygen_low or battery_low;
end Behavioral;