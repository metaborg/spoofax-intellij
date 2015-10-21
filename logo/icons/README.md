# Icons

This folder contains Spoofax icons.

![spoofax](spoofax_256.svg "spoofax")
![spoofax_eclipse](spoofax_eclipse_256.svg "spoofax_eclipse")
![spoofax_eclipse_blue](spoofax_eclipse_blue_256.svg "spoofax_eclipse_blue")
![spoofax_eclipse_cyan](spoofax_eclipse_cyan_256.svg "spoofax_eclipse_cyan")
![spoofax_eclipse_green](spoofax_eclipse_green_256.svg "spoofax_eclipse_green")
![spoofax_eclipse_orange](spoofax_eclipse_orange_256.svg "spoofax_eclipse_orange")
![spoofax_eclipse_red](spoofax_eclipse_red_256.svg "spoofax_eclipse_red")


## Creating an icon
To create your own icon, you need:

* Illustrator
* ImageMagick (`apt-get install imagemagick`)
* Inkscape (`apt-get install inkscape`)
* IcnsUtils (`apt-get install icnsutils`)

1. In Illustrator, create an artboard with a 256x256 pixel version of your icon.
2. Optionally, create artboards with optimized 32x32 and 16x16 versions too.
3. Name the artboards `256`, `32` and `16` respectively.
4. Save _all artboards_ from Illustrator as SVG files.

   > You should now have:
   > 
   > * `myicon.ai`
   > * `myicon_256.svg`
   > * `myicon_32.svg` (optional)
   > * `myicon_16.svg` (optional)
   >
   > You could also create 48x48, 64x64 and 128x128 SVG versions, but this
   > is not required. Any missing versions are generated from the 256x256
   > SVG.

5. Execute the `generate` script on your icon.

   ```
   generate myicon
   ```

   > This generates all PNG versions, and the ICO and ICNS files
   > in the `generated` subdirectory.


