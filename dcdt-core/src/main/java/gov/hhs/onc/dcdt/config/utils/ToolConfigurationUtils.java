package gov.hhs.onc.dcdt.config.utils;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanPropertyUtils;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
import gov.hhs.onc.dcdt.config.ConfigurationNode;
import gov.hhs.onc.dcdt.config.ConfigurationNode.ConfigurationNodeType;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.convert.ConversionService;

public abstract class ToolConfigurationUtils {
    public static String writeXml(HierarchicalConfiguration config) throws ConfigurationException {
        StringWriter strWriter = new StringWriter();
        writeXml(config, strWriter);

        return strWriter.toString();
    }

    public static void writeXml(HierarchicalConfiguration config, Writer writer) throws ConfigurationException {
        new XMLConfiguration(config).save(writer);
    }

    public static HierarchicalConfiguration build(Object obj) {
        return build(null, obj);
    }

    public static HierarchicalConfiguration build(@Nullable ConversionService convService, Object obj) {
        HierarchicalConfiguration config = new HierarchicalConfiguration();
        config.setRoot(buildNode(convService, getNodeName(obj.getClass()), obj));

        return config;
    }

    @SuppressWarnings({ "unchecked" })
    private static Node buildNode(@Nullable ConversionService convService, String nodeName, @Nullable Object nodeValue) {
        Node node = createNode(nodeName);

        if (nodeValue != null) {
            Class<?> nodeValueClass = nodeValue.getClass();
            ConfigurationNode nodeValueAnno = ToolAnnotationUtils.findAnnotation(ConfigurationNode.class, nodeValueClass);

            if (nodeValueAnno != null) {
                BeanWrapper nodeValueWrapper = ToolBeanUtils.wrap(nodeValue, convService);
                ConfigurationNode nodeValuePropAnno;
                Class<?> nodeValuePropValueClass, nodeValuePropValueEntryValueClass;
                String nodeValuePropName, nodeValuePropNodeName;
                ConfigurationNodeType nodeValuePropType;
                Collection<?> nodeValuePropValues, nodeValuePropValueEntryValues;
                Map<String, ?> nodeValuePropValueMap;
                Object nodeValuePropValueEntryValue;
                Node nodeValuePropValueNode;

                for (PropertyDescriptor nodeValuePropDesc : ToolBeanPropertyUtils.describeReadable(nodeValueWrapper)) {
                    if ((nodeValuePropAnno = ToolAnnotationUtils.findAnnotation(ConfigurationNode.class, nodeValuePropDesc.getReadMethod())) == null) {
                        continue;
                    }

                    nodeValuePropNodeName = getNodeName(nodeValuePropAnno, (nodeValuePropName = nodeValuePropDesc.getName()));

                    if (!(nodeValuePropType = nodeValuePropAnno.type()).isMap()) {
                        if (ToolClassUtils.isAssignable((nodeValuePropValueClass = nodeValuePropDesc.getPropertyType()), Collection.class)) {
                            nodeValuePropValues = ToolBeanPropertyUtils.getValue(nodeValueWrapper, nodeValuePropName, Collection.class);
                        } else if (nodeValuePropValueClass.isArray()) {
                            nodeValuePropValues = ToolArrayUtils.asList(((Object[]) ToolBeanPropertyUtils.getValue(nodeValueWrapper, nodeValuePropName)));
                        } else {
                            nodeValuePropValues = ToolArrayUtils.asList(ToolBeanPropertyUtils.getValue(nodeValueWrapper, nodeValuePropName));
                        }

                        if (nodeValuePropValues == null) {
                            continue;
                        }

                        // noinspection ConstantConditions
                        for (Object nodeValuePropValue : nodeValuePropValues) {
                            if (nodeValuePropValue == null) {
                                continue;
                            }

                            nodeValuePropValueNode = buildNode(convService, nodeValuePropNodeName, nodeValuePropValue);

                            if (nodeValuePropType.isAttribute()) {
                                node.addAttribute(nodeValuePropValueNode);
                            } else {
                                node.addChild(nodeValuePropValueNode);
                            }
                        }
                    } else if (!MapUtils.isEmpty((nodeValuePropValueMap =
                        ((Map<String, ?>) ToolBeanPropertyUtils.getValue(nodeValueWrapper, nodeValuePropName, Map.class))))) {
                        // noinspection ConstantConditions
                        for (String nodeValuePropValueEntryName : nodeValuePropValueMap.keySet()) {
                            if ((nodeValuePropValueEntryValue = nodeValuePropValueMap.get(nodeValuePropValueEntryName)) == null) {
                                continue;
                            }

                            if (ToolClassUtils.isAssignable((nodeValuePropValueEntryValueClass = nodeValuePropValueEntryValue.getClass()), Collection.class)) {
                                nodeValuePropValueEntryValues = ((Collection<?>) nodeValuePropValueEntryValue);
                            } else if (nodeValuePropValueEntryValueClass.isArray()) {
                                nodeValuePropValueEntryValues = ToolArrayUtils.asList(((Object[]) nodeValuePropValueEntryValue));
                            } else {
                                nodeValuePropValueEntryValues = ToolArrayUtils.asList(nodeValuePropValueEntryValue);
                            }

                            for (Object nodeValuePropValueEntryValueElem : nodeValuePropValueEntryValues) {
                                nodeValuePropValueNode = buildNode(convService, nodeValuePropValueEntryName, nodeValuePropValueEntryValueElem);

                                if (nodeValuePropType.isAttribute()) {
                                    node.addAttribute(nodeValuePropValueNode);
                                } else {
                                    node.addChild(nodeValuePropValueNode);
                                }
                            }
                        }
                    }
                }
            } else {
                node.setValue(nodeValue);
            }
        }

        return node;
    }

    private static String getNodeName(AnnotatedElement nodeAnnoElem) {
        return getNodeName(nodeAnnoElem, null);
    }

    private static String getNodeName(AnnotatedElement nodeAnnoElem, @Nullable String defaultNodeName) {
        return getNodeName(ToolAnnotationUtils.findAnnotation(ConfigurationNode.class, nodeAnnoElem), StringUtils.defaultIfBlank(defaultNodeName, StringUtils
            .uncapitalize(((nodeAnnoElem instanceof Class<?>) ? ToolClassUtils.getShortName(((Class<?>) nodeAnnoElem)) : ((Method) nodeAnnoElem).getName()))));
    }

    private static String getNodeName(ConfigurationNode nodeAnno, String defaultNodeName) {
        return StringUtils.defaultIfBlank(nodeAnno.name(), defaultNodeName);
    }

    private static Node createNode(String nodeName) {
        return createNode(nodeName, null);
    }

    private static Node createNode(String nodeName, @Nullable Object nodeValue) {
        return new Node(nodeName, nodeValue);
    }
}
