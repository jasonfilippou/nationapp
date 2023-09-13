I think the issue has to do with the order of instructions in the dump file.

I have tried avoiding dropping so that the foreign key constraints are not violated,
truncating (but the same foreign key constraints are then violated), but no dice.

Using `SOURCE` from the mysql prompt works like a charm. 