package com.nnroad.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.nnroad.extraAttribute.mapper.SysExtraAttributeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnroad.extraAttribute.domain.SysExtraAttribute;
import org.springframework.stereotype.Service;

@Service
public class ExtraAttributeUtils {
        @Autowired
        private SysExtraAttributeMapper sysExtraAttributeMapper;

        public boolean validateData(Object data, String tableName) {
            if (data == null) {
                return true; // or throw an exception
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(data);
            List<SysExtraAttribute> attributes = sysExtraAttributeMapper.selectExtraAttributeListByTableName(tableName);

            // Loop through the fields of the JsonNode
            Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();

            Map<Long, SysExtraAttribute> attributeMap = new HashMap<>();
            for (SysExtraAttribute attribute : attributes) {
                attributeMap.put(attribute.getId(), attribute);
            }
            //System.out.println(attributeMap);

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode value = field.getValue();
                SysExtraAttribute correspondingAttribute = attributeMap.get(Long.valueOf(key));

                // Validate type
                if (!correspondingAttribute.getType().toLowerCase().equals("json")) {
                    switch (correspondingAttribute.getType().toLowerCase()) {
                        case "string":
                            if (!value.isNull() && !value.isTextual()) {
                                System.out.println("not string");
                                return false;
                            }
                            // Validate length if applicable
                            Integer length = correspondingAttribute.getLength();
                            if (!value.isNull() && length != null && value.asText().length() > correspondingAttribute.getLength()) {
                                System.out.println("invalid length");
                                return false;
                            }
                            break;
                        case "text":
                            if (!value.isNull() && !value.isTextual()) {
                                System.out.println("not text");
                                return false;
                            }
                            break;
                        case "number":
                            if (!value.isNull() && !value.isInt() && !value.isDouble()) {
                                System.out.println("not number");
                                return false;
                            }
                            break;
                        case "boolean":
                            if (!value.isNull() && !value.asText().equals("true") && !value.asText().equals("false")) {
                                System.out.println("not boolean");
                                return false;
                            }
                            break;
                        case "date":
                            return true;
                        // Add more cases as needed
                        default:
                            System.out.println(correspondingAttribute.getType().toLowerCase() + " is not a valid type");
                            return false; // Invalid type
                    }
                } else {
                    if (!validateData(value, tableName)) {
                        System.out.println("Nested validation failed for key: " + key);
                        System.out.println(correspondingAttribute.getType().toLowerCase() + " is not a valid type");
                        return false;
                    }
                    continue;
                }
            }
            return true; // All validations passed

        }

    public Map<String, Object> processExtraDataForSearching(Object extraData) {
        ObjectMapper objectMapper = new ObjectMapper();
        String extraDataJson = (String) extraData;
        Map<String, Object> processedExtraData = new HashMap<>();
        try {
            // Deserialize the JSON string into a Map
            Map<String, Map<String, String>> extraDataMap = objectMapper.readValue(extraDataJson, HashMap.class);
            for (Map.Entry<String, Map<String, String>> entry : extraDataMap.entrySet()) {
                Map<String, Object> dataWithType = new HashMap<>();
                dataWithType.put("value", entry.getValue());
                dataWithType.put("type", entry.getValue() instanceof Map ? "nested" : "regular");
                processedExtraData.put(entry.getKey(), dataWithType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            System.out.println("Deserialized Map: " + processedExtraData);
        return processedExtraData;
    }

}

