#!/usr/bin/perl
use strict;
use warnings;

# set the record separate
$/ = "\n";

my $flag = 0;
while(<>) {
	if(/\/\*\*[^(*/)]*$/) {
		# we are on the first line of the comment block
		$flag = 1;
		s/(\/\*\*)\s*(\w+.*)$/$1\n * $2/;
	} elsif(/\*\//) {
		$flag = 0;
	} elsif($flag == 1) {
		# we are inside the comment block
		if(/\s+\*\s+\@/) {
			# @return OR @prama
			s/\s+\*\s+(\@\w+.*)$/ \*\t\t$1/;
			next;
		}
		unless(/^ \* \w+.*$/) {
			# * comments
			s/.*?(\w+.*)/$1/;
		}
	}
	print;
}
