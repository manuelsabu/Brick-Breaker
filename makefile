JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Breakout.java 

default: classes

classes: $(CLASSES:.java=.class)
run:
	java Breakout
clean:
	$(RM) *.class
