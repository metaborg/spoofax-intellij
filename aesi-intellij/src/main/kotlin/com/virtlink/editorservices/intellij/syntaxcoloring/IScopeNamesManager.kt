package com.virtlink.editorservices.intellij.syntaxcoloring

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.virtlink.editorservices.ScopeNames
import javax.swing.Icon


/**
 * Manages the scope names.
 *
 * A single scope name, which is an identifier of the form `a.b.c.d`, determines the coloring and style
 * of tokens.  For example, `entity.name.function.java` is the scope name for the name of a function in Java.
 * However, the scope manager could not possibly know all the possible scope names for all the possible languages.
 * Therefore, scope names get more specific with each additional identifier, i.e. `a` is the most general scope and
 * `a.b.c.d.e.f` is a very specific scope.  The scope manager will try to find a coloring and style for the most
 * specific scope name it knows, such as `a.b.c`, which is a prefix of the actual scope name.  For example, the scope
 * manager may know how to style `entity.name` in general, and apply that whenever it sees `entity.name.function.java`.
 *
 * As a special case, exact coloring and styling can be specified in a scope name.  They start with a dot (`.`),
 * followed by a sequence of tags, each separated by a dot.  Each tag specifies a property of the exact coloring
 * and styling.  The following tags are defined:
 *
 *     FC:#RRGGBB      (foreground color #RRGGBB)
 *     BC:#RRGGBB      (background color #RRGGBB)
 *     B               (bold)
 *     I               (italic)
 *     U               (underline)
 *     S               (strikethrough)
 *
 * For example, to color text red, bold and italic:
 *
 *     .FC:#FF0000.B.I
 *
 * Note: Tag names are case-insensitive.  Whitespace around the tag names and values should be avoided.
 */
interface IScopeNamesManager {

    /**
     * Returns the text attributes for the specified scope names.
     *
     * Note that the order of the returned attributes matters,
     * as they are later merged from first to last, where the attributes
     * of later attributes supersede those of earlier attributes.
     *
     * @param scopes The scope names.
     * @return An array of text attributes.
     */
    fun getTextAttributes(scopes: ScopeNames): Array<TextAttributesKey>

    /**
     * Returns the icon for an element with the specified scope names.
     *
     * @param scopes The scope names.
     * @return An icon; or null.
     */
    fun getIcon(scopes: ScopeNames): Icon?

    /**
     * Creates a [LookupElementBuilder] with the specified label
     * and styling based on the specified scope names.
     *
     * Note: the icon is not set.
     *
     * @param label The label.
     * @param scopes The scope names.
     * @return The created [LookupElementBuilder],
     * which can be configured further.
     */
    fun createLookupElement(label: String, scopes: ScopeNames): LookupElementBuilder

}