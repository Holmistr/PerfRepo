-----------------------------------------------------------------------------------------------
--                                                                                           --
-- Upgrade of db schema from version 1.3 to 1.4                                           --
--                                                                                           --
-----------------------------------------------------------------------------------------------

ALTER TABLE report_property ALTER COLUMN value type varchar(8192);

