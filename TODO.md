# Missing Features in Spoofax

Some features and behaviors in Spoofax Core could be changed to adapt
better to editors in general and AESI in particular.


## Token Categorization
Spoofax should support color schemes.  Instead of assigning a fixed
color to a token, Spoofax should allow the user to assign a name
to a token, such as `keyword` or `identifier`.  The names are then used
to find the appropriate coloring, depending on the current editor.


## Standalone Languages
Spoofax languages currently require the Spoofax Core library.  Instead,
if each language were to pack its own dependencies, they could be loaded
into an editor independently of one another.  This also removes the need
of Spoofax Core to deal with language loading and unloading.  As we
still want dynamic language loading, a (development) language could pack
additional code that allows it to be loaded dynamically.  Similarly,
we could add a plugin to the editor that can load a Spoofax language
dynamically, after which the language implementation takes over.


## Languages and Dialects
Currently, to determine the language or dialect, Spoofax Core looks at
the source filename.  Currently it only uses the file extension to
determine the language, and looks for a .meta file next to the source
file to determine the language dialect, if any.  Instead, the base
language should _only_ depend on the file extension, making it easier to
support various languages in an editor, as most editors also only
determine the language from the file extension.


## Virtual File System
Currently, Spoofax reads all files from disk.  It instead should use a
virtual file system, such that files that are open in the editor can be
read from the editor instead, where necessary.


## Custom Language Metadata
Every language should be able to specify a user-readable name
(e.g. `Stratego`), an icon to be used for the files, a short
description, and designate a default file extension (if any).
