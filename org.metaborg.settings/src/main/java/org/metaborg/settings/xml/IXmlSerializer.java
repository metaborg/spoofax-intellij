package org.metaborg.settings.xml;

import com.google.common.base.Preconditions;
import org.metaborg.settings.ISettingKey;
import org.metaborg.settings.Settings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;

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
