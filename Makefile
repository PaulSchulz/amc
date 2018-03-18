# Process documentation images

all: doc/images/field-of-view-full.svg doc/images/cross-section.svg

doc/images/cross-section.png: doc/images/cross-section.svg
	convert $< $@

doc/images/field-of-view-full.png: doc/images/field-of-view-full.svg
	convert $< $@
