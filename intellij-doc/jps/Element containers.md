# Element containers
A JPS element may expose an element container (an instance of `JpsElementContainer`). These elements extend the `JpsCompositeElement` class.

To be able to set and get an element in an element container, you need to get or defined a _role_ for your element. This is a unique singleton instance of the `JpsElementChildRole<E>` class, where `E` is the type of the element. The simplest is like this:

```
public static final JpsElementChildRole<ContosoElement> ROLE =
	JpsElementChildRoleBase.create("Contoso");
```

Now you can get the element from an element container.

```
JpsGlobal global;
ContosoElement myContosoElement =
	global.getContainer().getChild(ContosoElement.ROLE);
```

> **Note**: `getChild()` returns `null` when an element with the specified role could not be found.

Or set it.

```
JpsGlobal global;
global.getContainer().setChild(ContosoElement.ROLE, myContosoElement);
```

> **Note**: Setting an element sets the element's protected `myParent` variable to the element that owns the element container.

Or clear it.

```
JpsGlobal global;
global.getContainer().removeChild(ContosoElement.ROLE);
```

> **Note**: Removing an element sets the element's protected `myParent` variable to `null`.

