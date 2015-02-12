# MARFCATApp Build Makefile
# Use with GNU make.
#
# Serguei Mokhov
#
# COPYRIGHT (c) 2010-2012 The MARF Research Development Group
#
# $Id: Makefile,v 1.51 2014/06/19 15:47:00 mokhov Exp $
#

# Common vars
#VERSION=0.3.0-devel-`date +%Y%m%d`
VERSION=0.0.2-SATE-IV.5-`date +%Y%m%d`
TOOLSDIR=../../marf/src/tools
BASEDIR=../../..

# Dirs of MARF modules
MARFDIR = ../../marf/distro
MARFJAR = marf.jar

# Lists of all *.java and *.class files
JAVAFILES = *.java SATE/*.java Storage/*.java SATE/heuristics/lines/*.java
CLASSES = *.class
EXE = MARFCATApp

#
# Java Settings
#

JAVAC = javac
CLASSPATH = "$(BASEDIR):$(MARFJAR)"
JFLAGS = -g -source 1.5 -encoding UTF8 -verbose -deprecation -classpath $(CLASSPATH) -extdirs ".:$(MARFJAR)" -sourcepath $(BASEDIR)
JVM = java

JAVADOCFLAGS = -source 1.5 -verbose -use -version -author -classpath "$(BASEDIR):$(MARFJAR)"

#
# Build
#

all: $(EXE).class
	@echo "MARFCATApp build ok"

$(EXE).class: $(JAVAFILES)
	$(JAVAC) $(JFLAGS) $(EXE).java

jar: all
	( \
		cd $(BASEDIR) && \
		find -O3 -name "*.class" -print | grep -v tmp > classes.list && \
		jar cvmf marf/apps/MARFCAT/manifest.mf marfcat-$(VERSION)-fat.jar @classes.list \
	)
	ln -sf $(BASEDIR)/marfcat-$(VERSION)-fat.jar marfcat.jar
	@echo "MARFCAT $(VERSION) build has been successful"

#
# Run
#

run test-run train-run ident-run:
	@echo "$@ is ambiguous. Use specific 'make train-run-xyz' or 'make ident-run-xyz' instead."

# SOEN6611: experiment: Tomcat 13 -> GIPSY CVE classical
test-quick-gipsy-cve:
	time -p ./marfcat --train-test \
	    --super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml gipsy_test.xml $@ \
		" " \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SOEN6611 Quick CVE Tomcat 5.5.13-based GIPSY Testing Immediately Process has begun."

# SOEN6611: experiment: Tomcat 13 -> MARF CVE classical
test-quick-marf-cve:
	time -p ./marfcat --train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml marf_test.xml $@ \
		" " \
        > marfcat--super-fast-$@.log 2>&1 &
	@echo "The SOEN6611 Quick CVE Tomcat 5.5.13-based MARF Testing Immediately After Process has begun."


#
# Super-fast training testing for debugging
#

train-run-quick: all
	time -p ./marfcat --retrain \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.0_train.xml wireshark-1.2.0-train \
		" " \
		> marfcat--super-fast.log 2>&1 &
	@echo "The SATE 2010 Quick Training and Testing Process has begun."

# Quick Classical CVE

train-test-run-quick \
train-test-run-quick-wireshark \
train-test-run-quick-wireshark-cve \
	: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.0_train.xml $0 \
		" " \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Wireshark 1.2.0 CVE Training and Testing Immediately After Process has begun."

train-test-run-quick-chrome: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.54_train.xml chrome-5.0.375.54-train \
		" " \
		> marfcat--super-fast-chrome.log 2>&1 &
	@echo "The SATE 2010 Quick Chrome 54 Training and Testing Immediately After Process has begun."

train-test-run-quick-tomcat-cve: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-src_train.xml $@ \
		" " \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Tomcat 5.5.13 Training and Testing Immediately After Process has begun."

# Quick NLP CVE

train-test-run-quick-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.0_train.xml wireshark-1.2.0-train-cve-nlp \
		" " \
		> marfcat--nlp.log 2>&1 &
	@echo "The SATE 2010 Quick NLP Training and Testing Immediately After Process has begun."

train-test-run-quick-chrome-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.54_train.xml chrome-5.0.375.54-train-cve-nlp \
		" " \
		> marfcat-chrome--nlp.log 2>&1 &
	@echo "The SATE 2010 Quick Chrome 54 NLP Training and Testing Immediately After Process has begun."

train-test-run-quick-tomcat-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-train-cve-nlp \
		" " \
		> marfcat--super-fast-tomcat-train-cve-nlp.log 2>&1 &
	@echo "The SATE 2010 Quick Tomcat 13 NLP Training and Testing Immediately After Process has begun."

# Quick Classical CWE

train-test-run-quick-cwe-wireshark: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.0_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Wireshark 1.2.0 CWE Training and Testing Immediately After Process has begun."

train-test-run-quick-cwe-chrome: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.54_train.xml chrome-5.0.375.54-train-cwe \
		-cweid \
		> marfcat--super-fast-chrome-train-cwe.log 2>&1 &
	@echo "The SATE IV Quick Chrome 54 CWE Training and Testing Immediately After Process has begun."

train-test-run-quick-cwe-tomcat: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-train-cwe \
		-cweid \
		> marfcat--super-fast-tomcat-train-cwe.log 2>&1 &
	@echo "The SATE IV Quick Tomcat 13 CWE Training and Testing Immediately After Process has begun."

# Quick NLP CWE

train-test-run-quick-cwe-wireshark-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.0_train.xml wireshark-1.2.0-train-cwe-nlp \
		-cweid \
		> marfcat--super-fast-wireshark-train-cwe.log 2>&1 &
	@echo "The SATE IV Quick Wireshark 1.2.0 CWE NLP Training and Testing Immediately After Process has begun."

train-test-run-quick-cwe-chrome-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.54_train.xml chrome-5.0.375.54-train-cwe-nlp \
		-cweid \
		> marfcat--super-fast-chrome-train-cwe-nlp.log 2>&1 &
	@echo "The SATE IV Quick Chrome 54 CWE NLP Training and Testing Immediately After Process has begun."

train-test-run-quick-cwe-tomcat-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-train-cwe-nlp \
		-cweid \
		> marfcat--super-fast-tomcat-train-cwe-nlp.log 2>&1 &
	@echo "The SATE IV Quick Tomcat 13 CWE NLP Training and Testing Immediately After Process has begun."

# Quick Hall-Classical CWE
# Since SATE IV

train-test-run-quick-half-cwe-wireshark: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train-half.xml wireshark-1.2.0_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Wireshark 1.2.0 Half-CWE Training and Testing Immediately After Process has begun."

train-test-run-quick-half-cwe-chrome: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train-half.xml chrome-5.0.375.54_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Chrome 5.0.375.54 Half-CWE Training and Testing Immediately After Process has begun."

train-test-run-quick-half-cwe-tomcat: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train-half.xml apache-tomcat-5.5.13-src_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Tomcat 5.5.13 Half-CWE Training and Testing Immediately After Process has begun."

# Quick half-NLP CWE
# Since SATE IV

train-test-run-quick-half-cwe-wireshark-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		wireshark-1.2.0_train-half.xml wireshark-1.2.0_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV Quick Wireshark 1.2.0 Half-CWE NLP Training and Testing Immediately After Process has begun."

train-test-run-quick-half-cwe-chrome-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		chrome-5.0.375.54_train-half.xml chrome-5.0.375.54_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV Quick Chrome 5.0.375.54 Half-CWE NLP Training and Testing Immediately After Process has begun."

train-test-run-quick-half-cwe-tomcat-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train-half.xml apache-tomcat-5.5.13-src_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV Quick Tomcat 5.5.13 Half-CWE NLP Training and Testing Immediately After Process has begun."

#
# Testing on synthetic data (CWE only).
#

# C

# SATE IV: experiment: Synthetic C -> Synthetic C (sanity) CWE classical
train-synth-test-run-quick-synthetic-c-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-c_train.xml synthetic-c_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic C Training and Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Synthetic C Binary CWE classical
train-synth-test-run-quick-synthetic-c-cwe-bin: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-c-bin_train.xml synthetic-c-bin_train.xml $@ \
		"-cweid -binary" \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic Binary C Training and Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Synthetic C (sanity) CWE NLP
train-synth-test-run-synthetic-c-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-c_train.xml synthetic-c_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic C Training and Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Synthetic C Binary CWE NLP
train-synth-test-run-synthetic-c-cwe-nlp-bin: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-c-bin_train.xml synthetic-c-bin_train.xml $@ \
		"-cweid -binary" \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic Binary C Training and Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Wireshark 1.2.0 CWE classical
train-synth-test-run-quick-wireshark-1-2-0-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-c_train.xml wireshark-1.2.0_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic C Training and Wireshark 1.2.0 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Wireshark 1.2.0 CWE NLP
train-synth-test-run-wireshark-1-2-0-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-c_train.xml wireshark-1.2.0_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic C Training and Wireshark 1.2.0 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Wireshark 1.2.18 CWE classical
train-synth-test-run-quick-wireshark-1-2-18-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-c_train.xml wireshark-1.2.18_test.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic C Training and Wireshark 1.2.18 Testing Immediately After Process has begun."
	
# SATE IV: experiment: Synthetic C -> Wireshark 1.2.18 CWE NLP
train-synth-test-run-wireshark-1-2-18-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-c_train.xml wireshark-1.2.18_test.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic C Training and Wireshark 1.2.18 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Dovecot 1.2.0 CWE classical
train-synth-test-run-quick-dovecot-1-2-0-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-c_train.xml dovecot-1.2.0_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic C Training and Dovecot 1.2.0 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Dovecot 1.2.0 CWE NLP
train-synth-test-run-dovecot-1-2-0-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-c_train.xml dovecot-1.2.0_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic C Training and Dovecot 1.2.0 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Dovecot 1.2.17 CWE classical
train-synth-test-run-quick-dovecot-1-2-17-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-c_train.xml dovecot-1.2.17_test.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic C Training and Dovecot 1.2.17 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic C -> Dovecot 1.2.17 CWE NLP
train-synth-test-run-dovecot-1-2-17-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-c_train.xml dovecot-1.2.17_test.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic C Training and Dovecot 1.2.17 Testing Immediately After Process has begun."

# SATE IV: experiment: Wireshark 1.2.0 CWE classical -> Synthetic C
train-wireshark-1-2-0-test-run-quick-synth-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml synthetic-c_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Wireshark 1.2.0 Training and Synthetic C Testing Immediately After Process has begun."

# SATE IV: experiment: Wireshark 1.2.0 CWE NLP -> Synthetic C
train-wireshark-1-2-0-test-run-synth-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		wireshark-1.2.0_train.xml synthetic-c_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Wireshark 1.2.0 Training and Synthetic C Testing Immediately After Process has begun."

# SATE IV: experiment: Dovecot 1.2.0 CWE classical -> Synthetic C
train-dovecot-1-2-0-test-run-quick-synth-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		dovecot-1.2.0_train.xml synthetic-c_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Dovecot 1.2.0 Training and Synthetic C Testing Immediately After Process has begun."

# SATE IV: experiment: Dovecot 1.2.0 CWE NLP -> Synthetic C
train-dovecot-1-2-0-test-run-synth-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		dovecot-1.2.0_train.xml synthetic-c_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Dovecot 1.2.0 Training and Synthetic C Testing Immediately After Process has begun."

# SATE IV: experiment: Wireshark 1.2.0 + Dovecot 1.2.0 CWE classical -> Synthetic C
train-wireshark-1-2-0-dovecot-1-2-0-test-run-quick-synth-cwe: all
	time -p ./marfcat --retrain \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml synthetic-c_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Wireshark 1.2.0 Training Process has begun."
	sleep 600
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		dovecot-1.2.0_train.xml synthetic-c_train.xml $@ \
		-cweid \
		>> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Wireshark 1.2.0 + Dovecot 1.2.0 Training and Synthetic C Testing Immediately After Process has begun."

# SATE IV: experiment: Wireshark 1.2.0 + Dovecot 1.2.0 CWE NLP -> Synthetic C
train-wireshark-1-2-0-dovecot-1-2-0-test-run-quick-synth-cwe-nlp: all
	time -p ./marfcat --retrain \
		--nlp \
		$@ \
		wireshark-1.2.0_train.xml synthetic-c_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Wireshark 1.2.0 Training Process has begun."
	sleep 600
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		dovecot-1.2.0_train.xml synthetic-c_train.xml $@ \
		-cweid \
		>> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Wireshark 1.2.0 + Dovecot 1.2.0 Training and Synthetic C Testing Immediately After Process has begun."


# Java

# SATE IV: experiment: Synthetic Java -> Synthetic Java (sanity) CWE classical
train-synth-test-run-quick-synthetic-java-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-java_train.xml synthetic-java_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic Java Training and Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Synthetic Java Binary CWE classical
train-synth-test-run-quick-synthetic-java-cwe-bin: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-java-bin_train.xml synthetic-java-bin_train.xml $@ \
		"-cweid -binary"\
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic Java Binary Training and Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Synthetic Java (sanity) CWE NLP
train-synth-test-run-synthetic-java-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-java_train.xml synthetic-java_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic Java Training and Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Synthetic Java Binary CWE NLP
train-synth-test-run-synthetic-java-cwe-nlp-bin: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-java-bin_train.xml synthetic-java-bin_train.xml $@ \
		"-cweid -binary" \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic Binary Java Training and Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Tomcat 5.5.13 CWE classical
train-synth-test-run-quick-tomcat-5-5-13-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-java_train.xml apache-tomcat-5.5.13-src_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic Java Training and Tomcat 5.5.13 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Tomcat 5.5.13 CWE NLP
train-synth-test-run-tomcat-5-5-13-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-java_train.xml apache-tomcat-5.5.13-src_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic Java Training and Tomcat 5.5.13 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Tomcat 5.5.33 CWE classical
train-synth-test-run-quick-tomcat-5-5-33-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-java_train.xml apache-tomcat-5.5.33-src_test.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic Java Training and Tomcat 5.5.33 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Tomcat 5.5.33 CWE NLP
train-synth-test-run-tomcat-5-5-33-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-java_train.xml apache-tomcat-5.5.33-src_test.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic Java Training and Tomcat 5.5.33 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Jetty 6.1.16 CWE classical
train-synth-test-run-quick-jetty-6-1-16-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-java_train.xml jetty-6.1.16_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic Java Training and Jetty 6.1.16 Testing Immediately After Process has begun."
	
# SATE IV: experiment: Synthetic Java -> Jetty 6.1.16 CWE NLP
train-synth-test-run-jetty-6-1-16-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-java_train.xml jetty-6.1.16_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic Java Training and Jetty 6.1.16 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Jetty 6.1.26 CWE classical
train-synth-test-run-quick-jetty-6-1-26-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		synthetic-java_train.xml jetty-6.1.26_test.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Synthetic Java Training and Jetty 6.1.26 Testing Immediately After Process has begun."

# SATE IV: experiment: Synthetic Java -> Jetty 6.1.26 CWE NLP
train-synth-test-run-jetty-6-1-26-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		synthetic-java_train.xml jetty-6.1.26_test.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Synthetic Java Training and Jetty 6.1.26 Testing Immediately After Process has begun."

# SATE IV: experiment: Tomcat 5.5.13 CWE classical -> Synthetic Java
train-tomcat-5-5-13-test-run-quick-synth-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml synthetic-java_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Tomcat 5.5.13 Training and Synthetic Java Testing Immediately After Process has begun."

# SATE IV: experiment: Tomcat 5.5.13 CWE NLP -> Synthetic Java
train-tomcat-5-5-13-test-run-synth-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml synthetic-java_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Tomcat 5.5.13 Training and Synthetic Java Testing Immediately After Process has begun."

# SATE IV: experiment: Jetty 6.1.16 CWE classical -> Synthetic Java
train-jetty-6-1-16-test-run-quick-synth-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		jetty-6.1.16_train.xml synthetic-java_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Jetty 6.1.16 Training and Synthetic Java Testing Immediately After Process has begun."

# SATE IV: experiment: Jetty 6.1.16 CWE NLP -> Synthetic Java
train-jetty-6-1-16-test-run-synth-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		jetty-6.1.16_train.xml synthetic-java_train.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Jetty 6.1.16 Training and Synthetic Java Testing Immediately After Process has begun."

# SATE IV: experiment: Tomcat 5.5.13 + Jetty 6.1.16 CWE classical -> Synthetic Java
train-tomcat-5-5-13-jetty-6-1-16-test-run-quick-synth-cwe: all
	time -p ./marfcat --retrain \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml synthetic-java_train.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Tomcat 5.5.13 Training Process has begun."
	sleep 1200
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		jetty-6.1.16_train.xml synthetic-java_train.xml $@ \
		-cweid \
		>> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Tomcat 5.5.13 + Jetty 6.1.16 Training and Synthetic Java Testing Immediately After Process has begun."

# SATE IV: experiment: Tomcat 5.5.13 + Jetty 6.1.16 CWE NLP -> Synthetic Java
train-tomcat-5-5-13-jetty-6-1-16-test-run-quick-synth-cwe-nlp: all
	time -p ./marfcat --retrain \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml synthetic-java_train.xml $@ \
		-cweid \
		>> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Tomcat 5.5.13 Training Process has begun."
	sleep 3600
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		jetty-6.1.16_train.xml synthetic-java_train.xml $@ \
		-cweid \
		>> marfcat-$@.log 2>&1 &
	@echo "The SATE IV NLP CWE Tomcat 5.5.13 + Jetty 6.1.16 Training and Synthetic Java Testing Immediately After Process has begun."


#
# Testing on fixed data (*_test.xml)
#

# SATE: experiment: Wireshark 1.2.0 -> Wireshark 1.2.9 CVE classical
train-test-test-run-quick-wireshark-cve: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.9_test.xml $@ \
		" " \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE 2010 Quick CVE Wireshark 1.2.0 Training and Wireshark 1.2.9 Testing Immediately After Process has begun."

# SATE IV: experiment: Wireshark 1.2.0 -> Wireshark 1.2.18 CVE classical
train-test-test-run-quick-wireshark-1-2-18-cve: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.18_test.xml $@ \
		" " \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CVE Wireshark 1.2.0 Training and Wireshark 1.2.18 Testing Immediately After Process has begun."

# SATE IV: experiment: Wireshark 1.2.0 -> Wireshark 1.2.18 CVE NLP
train-test-test-run-quick-wireshark-1-2-18-cve-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.18_test.xml $@ \
		" " \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV CVE NLP Wireshark 1.2.0 Training and Wireshark 1.2.18 Testing Immediately After Process has begun."

# SATE IV: experiment: Wireshark 1.2.0 -> Wireshark 1.2.18 CWE classical
train-test-test-run-quick-wireshark-1-2-18-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.18_test.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Wireshark 1.2.0 Training and Wireshark 1.2.18 Testing Immediately After Process has begun."

# SATE IV: experiment: Wireshark 1.2.0 -> Wireshark 1.2.18 CWE NLP
train-test-test-run-quick-wireshark-1-2-18-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.18_test.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV CWE NLP Wireshark 1.2.0 Training and Wireshark 1.2.18 Testing Immediately After Process has begun."

# SATE: experiment: Chrome 54 -> Chrome 70 CVE classical
train-test-test-run-quick-chrome: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.70_test.xml chrome-5.0.375.70-test \
		" " \
		> marfcat--super-fast-chrome.log 2>&1 &
	@echo "The SATE 2010 Quick CVE Chrome 54 Training and Chrome 70 Testing Immediately After Process has begun."

# SATE: experiment: Chrome 54 -> Chrome 70 CVE NLP
train-test-test-run-quick-chrome-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.70_test.xml chrome-5.0.375.70-test-nlp \
		" " \
		> marfcat--super-fast-chrome-nlp.log 2>&1 &
	@echo "The SATE 2010 CVE NLP Chrome 54 Training and Chrome 70 Testing Immediately After Process has begun."

# SATE: experiment: Chrome 54 -> Chrome 70 CWE classical
train-test-test-run-quick-chrome54-chrome70-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.70_test.xml chrome-5.0.375.70-test-cwe \
		-cweid \
		> marfcat--super-fast-chrome54-chrome7-cwe.log 2>&1 &
	@echo "The SATE 2010 Quick CWE Chrome 54 Training and Chrome 70 Testing Immediately After Process has begun."

# SATE: experiment: Chrome 54 -> Chrome 70 CWE NLP
train-test-test-run-quick-chrome54-chrome70-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.70_test.xml chrome-5.0.375.70-test-cwe-nlp \
		-cweid \
		> marfcat--super-fast-chrome54-chrome70-cwe-nlp.log 2>&1 &
	@echo "The SATE 2010 CWE NLP Chrome 54 Training and Chrome 70 Testing Immediately After Process has begun."


# SATE: experiment: Tomcat 13 -> Tomcat 29 CVE classical
train-test-test-run-quick-tomcat-cve: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.29-src_test.xml $@ \
		" " \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE 2010 Quick CVE Tomcat 5.5.13 Training and Tomcat 5.5.29 Testing Immediately After Process has begun."

# SATE IV: experiment: Tomcat 5.5.13 -> Tomcat 5.5.33 CVE classical
train-test-test-run-quick-tomcat-5-5-33-cve: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.33-src_test.xml $@ \
		" " \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CVE Tomcat 5.5.13 Training and Tomcat 5.5.33 Testing Immediately After Process has begun."

# SATE: experiment: Tomcat 5.5.13 -> Tomcat 5.5.29 CVE NLP
train-test-test-run-quick-tomcat-5-5-29-cve-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.29-src_test.xml $@ \
		" " \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE 2010 CVE NLP Tomcat 5.5.13 Training and Tomcat 5.5.29 Testing Immediately After Process has begun."

# SATE IV: experiment: Tomcat 5.5.13 -> Tomcat 5.5.33 CVE NLP
train-test-test-run-quick-tomcat-5-5-33-cve-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.33-src_test.xml $@ \
		" " \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV CVE NLP Tomcat 5.5.13 Training and Tomcat 5.5.33 Testing Immediately After Process has begun."

# SATE: experiment: Tomcat 5.5.13 -> Tomcat 5.5.29 CWE classical
train-test-test-run-quick-tomcat-5-5-29-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.29-src_test.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE 2010 Quick CWE Tomcat 5.5.13 Training and Tomcat 5.5.29 Testing Immediately After Process has begun."

# SATE IV: experiment: Tomcat 5.5.13 -> Tomcat 5.5.33 CWE classical
train-test-test-run-quick-tomcat-5-5-33-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.33-src_test.xml $@ \
		-cweid \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick CWE Tomcat 5.5.13 Training and Tomcat 5.5.33 Testing Immediately After Process has begun."

# SATE: experiment: Tomcat 5.5.13 -> Tomcat 5.5.29 CWE NLP
train-test-test-run-quick-tomcat-5-5-29-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.29-src_test.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE 2010 CWE NLP Tomcat 5.5.13 Training and Tomcat 5.5.29 Testing Immediately After Process has begun."

# SATE: experiment: Tomcat 5.5.13 -> Tomcat 5.5.33 CWE NLP
train-test-test-run-quick-tomcat-5-5-33-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.33-src_test.xml $@ \
		-cweid \
		> marfcat-$@.log 2>&1 &
	@echo "The SATE IV CWE NLP Tomcat 5.5.13 Training and Tomcat 5.5.33 Testing Immediately After Process has begun."


train-test-test-run-quick-wireshark-chrome-cve: all
	time -p ./marfcat --retrain \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.9_test.xml wireshark-1.2.9-test \
		" " \
		> marfcat--super-fast-wireshark-chrome-cve.log 2>&1 &
	time -p ./marfcat --retrain \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.70_test.xml chrome-5.0.375.70-test \
		" " \
		>> marfcat--super-fast-wireshark-chrome-cve.log 2>&1 &
	@echo "The SATE 2010 Quick Training and Testing Process has begun."

train-test-test-run-quick-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.9_test.xml wireshark-1.2.9-test \
		-cweid \
		> marfcat--super-fast-wireshark.log 2>&1 &
	@echo "The SATE 2010 Quick CWE Training and Testing Immediately After Process has begun."


# SATE: experiment: Wireshark 1.2.0 -> Dovecot classical (CWE)
train-test-test-run-quick-dovecot-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml dovecot-2.0.beta6_test.xml dovecot-2.0.beta6-wireshark-test-cwe \
		-cweid \
		> marfcat--super-fast-dovecot-wireshark-test-cwe.log 2>&1 &
	@echo "The SATE 2010 Quick CWE Wireshark Training and Dovecot Testing Immediately After Process has begun."


# SATE: experiment: Chrome 54 -> Dovecot classical (CWE)
train-test-test-run-quick-dovecot-cwe-chrome: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml dovecot-2.0.beta6_test.xml dovecot-2.0.beta6-chrome-test \
		-cweid \
		> marfcat--super-fast-dovecot-cwe-chrome.log 2>&1 &
	@echo "The SATE 2010 Quick CWE Chrome 54 Training and Dovecot Testing Immediately After Process has begun."

# SATE: experiment: Chrome 54 -> Dovecot NLP (CWE)
train-test-test-run-quick-dovecot-cwe-chrome-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		chrome-5.0.375.54_train.xml dovecot-2.0.beta6_test.xml dovecot-2.0.beta6-chrome-test \
		-cweid \
		> marfcat--super-fast-dovecot-cwe-chrome-nlp.log 2>&1 &
	@echo "The SATE 2010 NLP CWE Chrome 54 Training and Dovecot Testing Immediately After Process has begun."

# SATE: experiment: Chrome 54 -> Wireshark 1.2.0 classical (CWE)
train-test-test-run-quick-chrome54-wireshark120-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml wireshark-1.2.0_test.xml wireshark-1.2.0-chrome-test \
		-cweid \
		> marfcat--super-fast-chrome54-wireshark120-cwe.log 2>&1 &
	@echo "The SATE 2010 Quick CWE Chrome 54 Training and Wireshark 1.2.0 Testing Immediately After Process has begun."

# SATE: experiment: Chrome 54 -> Wireshark 1.2.0 NLP (CWE)
train-test-test-run-quick-chrome54-wireshark120-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		chrome-5.0.375.54_train.xml wireshark-1.2.0_test.xml wireshark-1.2.0-chrome-test \
		-cweid \
		> marfcat--super-fast-chrome54-wireshark120-cwe-nlp.log 2>&1 &
	@echo "The SATE 2010 NLP CWE Chrome 54 Training and Wireshark 1.2.0 Testing Immediately After Process has begun."

# SATE: experiment: Chrome 54 -> Wireshark 1.2.9 classical (CWE)
train-test-test-run-quick-chrome54-wireshark129-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml wireshark-1.2.9_test.xml wireshark-1.2.9-chrome-test \
		-cweid \
		> marfcat--super-fast-chrome54-wireshark129-cwe.log 2>&1 &
	@echo "The SATE 2010 Quick CWE Chrome 54 Training and Wireshark 1.2.9 Testing Immediately After Process has begun."

# SATE: experiment: Chrome 54 -> Wireshark 1.2.9 NLP (CWE)
train-test-test-run-quick-chrome54-wireshark129-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		chrome-5.0.375.54_train.xml wireshark-1.2.9_test.xml wireshark-1.2.9-chrome-test \
		-cweid \
		> marfcat--super-fast-chrome54-wireshark129-cwe-nlp.log 2>&1 &
	@echo "The SATE 2010 NLP CWE Chrome 54 Training and Wireshark 1.2.9 Testing Immediately After Process has begun."


# SATE: experiment: Tomcat 13 -> Pebble CVE classical
train-test-test-run-quick-tomcat13-pebble-cve: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml pebble_test.xml pebble-test-cve \
		" " \
		> marfcat--super-fast-tomcat13-pebble-cve.log 2>&1 &
	@echo "The SATE 2010 Quick CVE Tomcat 13 Training and Pebble Testing Immediately After Process has begun."

# SATE: experiment: Tomcat 13 -> Pebble CVE NLP
train-test-test-run-quick-tomcat13-pebble-cve-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml pebble_test.xml pebble-test-cve-nlp \
		" " \
		> marfcat--super-fast-tomcat13-pebble-cve-nlp.log 2>&1 &
	@echo "The SATE 2010 CVE NLP Tomcat 13 Training and Pebble Testing Immediately After Process has begun."

# SATE: experiment: Tomcat 13 -> Pebble CWE classical
train-test-test-run-quick-tomcat13-pebble-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml pebble_test.xml pebble-test-cwe \
		-cweid \
		> marfcat--super-fast-tomcat13-pebble-cwe.log 2>&1 &
	@echo "The SATE 2010 Quick CWE Tomcat 13 Training and Pebble Testing Immediately After Process has begun."

# SATE: experiment: Tomcat 13 -> Pebble CWE NLP
train-test-test-run-quick-tomcat13-pebble-cwe-nlp: all
	time -p ./marfcat --retrain-train-test \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml pebble_test.xml pebble-test-cwe-nlp \
		-cweid \
		> marfcat--super-fast-tomcat13-pebble-cwe-nlp.log 2>&1 &
	@echo "The SATE 2010 CWE NLP Tomcat 13 Training and Pebble Testing Immediately After Process has begun."


train-test-test-run-quick-chrome-cwe: all
	time -p ./marfcat --retrain-train-test \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml chrome-5.0.375.54_test.xml chrome-5.0.375.54-test \
		-cweid \
		> marfcat--super-fast.log 2>&1 &
	@echo "The SATE 2010 Quick CWE Wireshark Training and Chrome 54 Testing Immediately After Process has begun."

#
# Statistics reporting
#

# SATE IV: Synthetic C Classical CWE Stats
train-synth-test-run-quick-synthetic-c-cwe-stats \
train-wireshark-1-2-0-test-run-quick-synth-cwe-stats \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		synthetic-c_train.xml synthetic-c_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

# SATE IV: Synthetic C Classical Binary CWE Stats
train-synth-test-run-quick-synthetic-c-bin-cwe-stats \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		synthetic-c-bin_train.xml synthetic-c-bin_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

# SATE IV: Synthetic C NLP CWE Stats
train-synth-test-run-synthetic-c-cwe-nlp-stats \
train-wireshark-1-2-0-test-run-synth-cwe-nlp-stats \
	: all
	./marfcat --stats \
		--nlp \
		$@ \
		synthetic-c_train.xml synthetic-c_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

# SATE IV: Synthetic Java Classical CWE Stats
train-synth-test-run-quick-synthetic-java-cwe-stats \
train-synth-test-run-quick-tomcat-5-5-13-cwe-stats \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		synthetic-java_train.xml synthetic-java_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

# SATE IV: Synthetic Java Classical Binary CWE Stats
train-synth-test-run-quick-synthetic-java-bin-cwe-stats \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		synthetic-java-bin_train.xml synthetic-java-bin_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

# SATE IV: Synthetic Java NLP CWE Stats
train-synth-test-run-synthetic-java-cwe-nlp-stats \
train-synth-test-run-tomcat-5-5-13-cwe-nlp-stats \
	: all
	./marfcat --stats \
		--nlp \
		$@ \
		synthetic-java_train.xml synthetic-java_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

train-run-quick-stats \
train-run-quick-stats-wireshark \
train-test-run-quick-wireshark-cve-stats \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.0_train.xml $@ \
		" " \
		>> marfcat-stats-$@.log 2>&1

train-run-quick-stats-chrome: all
	./marfcat --stats \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.54_train.xml $@ \
		" " \
		>> marfcat-stats-$@.log 2>&1

train-run-quick-stats-tomcat \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-src_train.xml $@ \
		" " \
		>> marfcat-stats-$@.log 2>&1

train-tomcat-5-5-13-test-run-quick-synth-cwe-stats \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-src_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

train-tomcat-5-5-13-test-run-synth-cwe-nlp-stats \
	: all
	./marfcat --stats \
		--nlp \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-src_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

train-run-quick-stats-cwe \
train-run-quick-stats-wireshark-cwe \
train-test-run-quick-cwe-wireshark-stats \
train-run-quick-stats-wireshark-half-cwe \
train-synth-test-run-quick-wireshark-1-2-0-cwe-stats \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.0_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

train-synth-test-run-wireshark-1-2-0-cwe-nlp-stats \
	: all
	./marfcat --stats \
		--nlp \
		$@ \
		wireshark-1.2.0_train.xml wireshark-1.2.0_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

train-run-quick-stats-chrome-cwe \
train-run-quick-stats-chrome-half-cwe \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		chrome-5.0.375.54_train.xml chrome-5.0.375.54_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

train-run-quick-stats-tomcat-cwe \
train-run-quick-stats-tomcat-half-cwe \
	: all
	./marfcat --stats \
		--super-fast \
		$@ \
		apache-tomcat-5.5.13-src_train.xml apache-tomcat-5.5.13-src_train.xml $@ \
		-cweid \
		>> marfcat-stats-$@.log 2>&1

train-synth-test-run-quick-wireshark-1-2-18-cwe-stats \
train-synth-test-run-wireshark-1-2-18-cwe-nlp-stats \
train-synth-test-run-quick-dovecot-1-2-17-cwe-stats \
train-synth-test-run-dovecot-1-2-17-cwe-nlp-stats \
train-synth-test-run-quick-tomcat-5-5-33-cwe-stats \
train-synth-test-run-tomcat-5-5-33-cwe-nlp-stats \
train-synth-test-run-quick-jetty-6-1-26-cwe-stats \
train-synth-test-run-jetty-6-1-26-cwe-nlp-stats \
	:
	@echo "No stats possible for $@"

train-synth-test-run-quick-dovecot-1-2-0-cwe-stats \
train-synth-test-run-dovecot-1-2-0-cwe-nlp-stats \
train-dovecot-1-2-0-test-run-quick-synth-cwe-stats \
train-dovecot-1-2-0-test-run-synth-cwe-nlp-stats \
train-wireshark-1-2-0-dovecot-1-2-0-test-run-quick-synth-cwe-stats \
train-wireshark-1-2-0-dovecot-1-2-0-test-run-quick-synth-cwe-nlp-stats \
train-synth-test-run-quick-jetty-6-1-16-cwe-stats \
train-synth-test-run-jetty-6-1-16-cwe-nlp-stats \
train-jetty-6-1-16-test-run-quick-synth-cwe-stats \
train-jetty-6-1-16-test-run-synth-cwe-nlp-stats \
train-tomcat-5-5-13-jetty-6-1-16-test-run-quick-synth-cwe-stats \
train-tomcat-5-5-13-jetty-6-1-16-test-run-quick-synth-cwe-nlp-stats \
	:
	@echo "No stats available yet for $@"


# SATE IV: experiment: Malware pcap on Malware pcap Raw
train-test-run-quick-malware-pcap-raw-bin: all
	time -p ./marfpcat --retrain-train-test \
		--super-fast \
		$@ \
		malware-backup01_02_2012-990000-1000000_train.xml malware-backup01_02_2012-990000-1000000_test.xml $@ \
		"-dynaclass -binary" \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Malware Raw Pcap Binary Training and Testing Immediately After Process has begun."

train-test-run-quick-malware-pcap-raw-bin-all-headers: all
	time -p ./marfpcat --retrain-train-test \
		--super-fast \
		$@ \
		malware-backup01_02_2012-990000-1000000_train.xml malware-backup01_02_2012-990000-1000000_test.xml $@ \
		"-dynaclass -binary --headers-only" \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Malware Raw Pcap Header Training and Testing Immediately After Process has begun."
train-test-run-quick-malware-pcap-raw-bin-29-headers: all
	time -p ./marfpcat --retrain-train-test \
		--super-fast \
		$@ \
		malware-backup01_02_2012-990000-1000000_train.xml malware-backup01_02_2012-990000-1000000_test.xml $@ \
		"-dynaclass -binary --headers-features" \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Malware Raw Pcap 29 Header FeaturesTraining and Testing Immediately After Process has begun."
train-test-run-quick-malware-pcap-raw-bin-best-headers: all
	time -p ./marfpcat --retrain-train-test \
		--super-fast \
		$@ \
		malware-backup01_02_2012-990000-1000000_train.xml malware-backup01_02_2012-990000-1000000_test.xml $@ \
		"-dynaclass -binary --best-headers-features" \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Malware Raw Pcap Best Headers Training and Testing Immediately After Process has begun."
train-test-run-quick-malware-pcap-raw-bin-all-flows: all
	time -p ./marfpcat --retrain-train-test \
		--super-fast \
		$@ \
		malware-backup01_02_2012-990000-1000000_train.xml malware-backup01_02_2012-990000-1000000_test.xml $@ \
		"-dynaclass -binary --flow-features" \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Malware Raw Pcap All Flows Training and Testing Immediately After Process has begun."
train-test-run-quick-malware-pcap-raw-bin-best-flows: all
	time -p ./marfpcat --retrain-train-test \
		--super-fast \
		$@ \
		malware-backup01_02_2012-990000-1000000_train.xml malware-backup01_02_2012-990000-1000000_test.xml $@ \
		"-dynaclass -binary --best-flow-features" \
		> marfcat--super-fast-$@.log 2>&1 &
	@echo "The SATE IV Quick Malware Raw Pcap Best Flows Training and Testing Immediately After Process has begun."

train-test-run-quick-malware-pcap-raw-bin-stats \
train-test-run-quick-malware-pcap-raw-bin-all-headers-stats \
train-test-run-quick-malware-pcap-raw-bin-29-headers-stats \
train-test-run-quick-malware-pcap-raw-bin-best-headers-stats \
train-test-run-quick-malware-pcap-raw-bin-all-flows-stats \
train-test-run-quick-malware-pcap-raw-bin-best-flows-stats \
	: all
	./marfpcat --stats \
		--super-fast \
		$@ \
		malware-backup01_02_2012-990000-1000000_train.xml malware-backup01_02_2012-990000-1000000_test.xml $@ \
		"-dynaclass -binary" \
		>> marfcat-stats-$@.log 2>&1
#
# Javadoc
#

javadoc-html: $(JAVAFILES)
	@echo "Compiling javadoc..."
	mkdir -p html
	javadoc $(JAVADOCFLAGS) -private -d html $(JAVAFILES)


#
# Clean Up
#

clean:
	rm -f $(CLASSES) *~

maintainer-clean: clean
	rm -f *.stats *log training.set output.txt *.diff *.bin *.xml *.gzbin \
		stats.txt stats-date.tex best-score.tex coding.html COPYRIGHT $(EXE).jar
	rm -f training-samples/*.txt training-samples/*.ppm testing-samples/*.txt testing-samples/*.ppm

# EOF
