# Assembly Checksum

This folder contains the low-level checksum routine used by the simulator.

Purpose:

1. Demonstrate low-level packet integrity logic.
2. Support verification that transmitted data was not corrupted.

The checksum is calculated over the canonical packet string shared with the Java backend.