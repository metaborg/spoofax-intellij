# Metaborg Settings

If you have settings you want to read and write, this library is for
you. It allows you to easily read, write and manage settings for
anything that has settings, such as your application or an element of
your application.

Settings are stored in a `Settings` object, which are part of an
tree of `Settings` objects. You can query the object for a particular
setting, and it will retrieve it for you. If the setting cannot be found
in the current `Settings` object, it will continue looking for it in a
parent `Settings` object. This allows you to, for example, provide
default settings as the top-level `Settings` object, and let lower-level
`Settings` objects override these settings.

You have full control over how a setting's value is inherited from a
parent. By default a child setting overrides a parent setting, but for
lists and collections you usually want a child's value to append to
the parent's value.

The `Settings` objects are read and written using a settings format.
Built-in settings formats are YAML and Maven, but you can define your
own.

> **Example**:
> 
> ```
> 
> ```

## Building
To build and install this library in your local Maven repository,
execute Gradle like this:

```
./gradlew clean install
```

