MCU = atmega169
TARGET = target
MAKEFILE = /home/albertnet/workspace/c/Makefile

SRCDIR=src
IDIR=/usr/lib/avr/include
LIBDIR=/usr/lib/avr/lib
OUTPUTDIR=obj
DENDIR = $(OUTPUTDIR)/dep
SOURCES = $(notdir $(wildcard $(SRCDIR)/*))
OBJECTS = $(addprefix $(OUTPUTDIR)/,$(subst .c,.o,$(SOURCES)))

CPPFLAGS +=-I/usr/lib/avr/include -Iinclude
COMPILEOPTION=-mmcu=$(MCU) $(CPPFLAGS) -std=c99 -Os
LINKOPTION=-mmcu=$(MCU) -Os

CC=avr-gcc
OBJCOPY=avr-objcopy
SORT=sort
AWK=awk
CAT=cat
BUILDHEADER=/home/albertnet/workspace/python/example/buildHeader.py

vpath %.c src
vpath %.h include
vpath %.o obj
vpath %.d obj/dep


all: compile
	avrdude -c avrispmkII -p m169 -P usb  -U flash:w:$(OUTPUTDIR)/$(TARGET).hex

compile: $(OUTPUTDIR)/$(TARGET).hex
	
$(OUTPUTDIR)/$(TARGET).hex: $(OUTPUTDIR)/$(TARGET).out
	$(OBJCOPY) -O ihex -R .eeprom $< $@

$(OUTPUTDIR)/$(TARGET).out : $(OBJECTS)
	$(CC) $(LINKOPTION) $^ -lm -o $@

$(OUTPUTDIR)/%.o : %.c
	$(CC) -c $(COMPILEOPTION) $< -o $@

include/%.h : %.c
	$(BUILDHEADER) . $<
	touch $@

$(DENDIR)/%.d : %.c
	$(CC) -M $(CPPFLAGS) $< > $@.$$$$; \
	sed 's!\($*\)\.o[ :]*!$(OUTPUTDIR)/\1.o $@ : !g' < $@.$$$$ > $@; \
	$(RM) -f $@.$$$$

ifneq "$(strip $(MAKECMDGOALS))" "help"
ifneq "$(strip $(MAKECMDGOALS))" "clean"
-include $(shell mkdir $(DENDIR) 2>/dev/null) $(addprefix $(DENDIR)/,$(subst .c,.d,$(SOURCES)))
endif
endif

.PHONY: clean
clean:
	rm -rf $(OUTPUTDIR)/*

.PHONY: help
help:
	@$(CAT) "$(MAKEFILE)" | $(AWK) '/^[^.%][-A-Za-z0-9_]*:/ { print substr($$1, 1, length($$1)-1) }' | $(SORT)
