SimpleHealth v0.3.1===================Commandless health regeneration for groups* by krinsdeathFeatures--------*   SuperPerms support (recommended use with PermissionsBukkit)*   Configurable health regeneration, based on groups*   Event-driven health management. For example, you can change how    much health a player respawns with.*   Item based health management. Change how much health an item gives    (or takes!), per group.*   Command shadowing. Disable commands for groups, even if they're    not registered with permissions.Changelog---------*   Version 0.3.1:*   1   Removed all NMS dependency.*   Version 0.3.0:*   1   Added SuperPerms support; permissions nodes remain unaffected*   2   Fixed possible threading issues by using Scheduler instead of TimerTask*   Version 0.2.0:*   1   Renamed to SimpleHealth*   2   Added items keys to the configuration. You can now change the default health         restore rate of items, and even specify blocks, which can then be "eaten" for health!*   3   Cleaned up the code and added some localization options*   4   Added a flag to control regeneration on peaceful mode servers. Specify        which worlds you want to apply peaceful mode to, and the plugin will handle it for you!*   Version 0.1.2:*   1   Fixed peaceful mode and non-peaceful mode overlapping and causing problems*   2   Cleaned up code*   Version 0.1.1:*   1   Fixed max health overflow*   Version 0.1.0a:*   1   Initial release