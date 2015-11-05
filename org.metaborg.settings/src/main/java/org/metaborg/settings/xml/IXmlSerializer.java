package org.metaborg.settings.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * XML serializer.
 */
public interface IXmlSerializer {

    /**
     * Serializes an object.
     */
    Node serialize(
            final Object value,
            final Document doc,
            final XmlSettingsFormat format);

}
