#!/usr/bin/perl -w

# Author: Serguei Mokhov
# $Id: collect-files-meta.pl,v 1.8 2012/01/03 04:16:26 mokhov Exp $

use strict;
use DateTime;

# Find all regular files in restricted set of directories
# Go through each file in the file list
# Take its type through `file'
# Hash the name by type -> {list of file names}
# When hashtable is built, traverse through its keys
#   and build the sequence of IDs, type names, and
#   training and testing files (sets are identical
#   in this case)

my @astrRegularFiles = ();
my %haFileTypes      = ();

my $bTresholdEnabled = 0;
my $iTresholdLimit = 200;
my $iTreshold = 0;

#print "ARGV[1]=$ARGV[0]\n";
die "Usage: $0 <dir> ($!)" if !defined($ARGV[0]) || !(-d $ARGV[0]);

# SATE4 XSD:
# (/usr/include|synthetic|dovecot-1.2.(0|17)|wireshark-1.2.(0|18)|apache-tomcat-5.5.(13|33)-src|jetty-6.1.(16|26)|wordpress-2.(0|2.3))(/[A-Za-z0-9_\.\-]+)*/[A-Za-z0-9_\.\-]+\.(java|jsp|jspf|js|nsi|c|cpp|cc|cxx|hxx|inc|h|xml|html|php)
@astrRegularFiles =
(
	@astrRegularFiles,
	`find -L $ARGV[0] -regextype posix-egrep -type f -iregex ".*.java|.*.jsp|.*.jspf|.*.js|.*.nsi|.*.c|.*.cpp|.*.cc|.*.cxx|.*.hxx|.*.inc|.*.h|.*.xml|.*.html|.*.php"`
);

# @astrRegularFiles =
# (
# 	@astrRegularFiles,
# 	`find -L $ARGV[0] -regextype posix-egrep -type f -iregex ".*\.java|.*\.jsp|.*\.js|.*\.nsi|.*\.c|.*\.cpp|.*\.cc|.*\.cxx|.*\.hxx|.*\.inc|.*\.h|.*\.xml|.*\.html"`
# );


my $strOutFile = "$ARGV[0]_test.xml";
my $strDate = DateTime->now;

# XXX: Should use /usr/bin/file as it is newer
my $fileUtilityVersion = `file --version`;
my $findUtilityVersion = `find --version`;
my $marfUtilityVersion = `java -jar marf.jar --diagnostic`;


open(XMLOUT, ">", "$strOutFile")
	or die "$!";

print XMLOUT <<XMLHEAD
<?xml version="1.0" encoding="UTF-8"?>
<dataset generated-by="$0" generated-on="$strDate">
	<description>
		<file-type-tool>
$fileUtilityVersion
		</file-type-tool>
		<find-tool>
$findUtilityVersion
		</find-tool>
		<marf-tool>
$marfUtilityVersion
		</marf-tool>
	</description>
XMLHEAD
;

my $iID = 1;

foreach my $strFile (@astrRegularFiles)
{
	if($bTresholdEnabled)
	{
		if($iTreshold > $iTresholdLimit)
		{
			last;
		}
	}

	# `find` returns filenames with \n in them
	chomp($strFile);

	my $strFileType = `file $strFile`;
	chomp($strFileType);
	($strFile, $strFileType) = split(/: /, $strFileType);
	$strFileType =~ s/,/;/g;

	my $strLexiCounts = `wc $strFile`;
	chomp($strLexiCounts);
	my (undef, $iLineCount, $iWordCount, $iByteCount) = split(/\s+/, $strLexiCounts);

	print XMLOUT<<PATHID
	<file id="$iID" path="$strFile">
		<meta>
			<type>$strFileType</type>
			<length lines="$iLineCount" words="$iWordCount" bytes="$iByteCount" />
		</meta>
		<location line="" fraglines="">
			<meta>
				<cve></cve>
				<name cweid=""></name>
			</meta>
			<fragment>
			</fragment>
			<explanation>
			</explanation>
		</location>
	</file>
PATHID
;

#	if(!defined($haFileTypes{$strFileType}))
#	{
#		#$haFileTypes{$strFileType} = ();
#		$haFileTypes{$strFileType} = "$strFile";
#	}
#	else
#	{
#		#print "Before: [[$haFileTypes{$strFileType}]]\n";
#		#$haFileTypes{$strFileType} = ($haFileTypes{$strFileType}, $strFile);
#		$haFileTypes{$strFileType} = "$haFileTypes{$strFileType}|$strFile";
#		#print "After: [[$haFileTypes{$strFileType}]]\n\n";
#	}
#	#print "$strFile:[$strFileType]\n";
#
	$iTreshold++;
	$iID++;
}

#print "0,Unknown\n";
#
#my $iID = 1;
#
#foreach my $strFileType (keys(%haFileTypes))
#{
#	#my $strFileSet = join("|", $haFileTypes{$strFileType});#
#	my $strFileSet = $haFileTypes{$strFileType};
#	print "$iID,$strFileType,$strFileSet,$strFileSet\n";
#	$iID++;
#}

print XMLOUT <<XMLTAIL
</dataset>
XMLTAIL
;

close(XMLOUT)
	or die "$!";

exit(0);

# EOF
