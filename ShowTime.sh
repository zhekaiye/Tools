#!/bin/sh
#! ./mycmd infile outfile
awk '{printf $0; \
if(NR==1){flag=$0}; \
printf strftime("     %Y-%m-%d %H:%M:%S::", substr($0, 1, 10)); \
printf substr($0, 11, 13); \
print "    ", int(($0-flag)/3600000), "h", int(($0-flag)%3600000/60000), "m", ($0-flag)%60000/1000, "s"; \
flag=$0} \
END {print "There are", FNR, "items in all."}' $1 > $2