package com.nnroad.system.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnroad.extraAttribute.domain.SysExtraAttribute;
import com.nnroad.extraAttribute.mapper.SysExtraAttributeMapper;

public class SysServiceUtil {
	@Autowired
    private static SysExtraAttributeMapper sysExtraAttributeMapper;
	
	public static boolean validateData(Object data) {
    	if (data == null) {
            return true; // or throw an exception
        }
    	ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(data);
        String tableName = "sys_client_attribute";
        List<SysExtraAttribute> attributes = sysExtraAttributeMapper.selectExtraAttributeListByTableName(tableName);
        //System.out.println(attributes);
        
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
            System.out.println(key);
            SysExtraAttribute correspondingAttribute = attributeMap.get(Long.valueOf(key));
            System.out.println(key + " " + value.asText()) ;
            
            // Validate type
            if (!correspondingAttribute.getType().toLowerCase().equals("json")) {
                switch (correspondingAttribute.getType().toLowerCase()) {
                    case "string":
                    	System.out.println("string");
                        if (!value.isTextual()) {
                        	System.out.println("not string");
                            return false;
                        }
                        // Validate length if applicable
                        Integer length = correspondingAttribute.getLength();
                        if (length != null && value.asText().length() > correspondingAttribute.getLength()) {
                        	System.out.println("invalid length");
                            return false;
                        }
                        break;
                    case "integer":
                        if (!value.isInt()) {
                        	System.out.println("not integer");
                            return false;
                        }
                        break;
                    case "boolean":
                        if (!value.isBoolean()) {
                        	System.out.println("not boolean");
                            return false;
                        }
                        break;
                    // Add more cases as needed
                    default:
                        return false; // Invalid type
                }
            } else {
            	if (!validateData(value)) {
                    System.out.println("Nested validation failed for key: " + key);
                    return false;
                }
            	continue;
            }
        }
        return true; // All validations passed

    }
}
