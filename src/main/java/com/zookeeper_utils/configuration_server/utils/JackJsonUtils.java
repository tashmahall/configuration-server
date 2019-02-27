package com.zookeeper_utils.configuration_server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JackJsonUtils {

	private static ObjectMapper objectMapper = init();
	public static ObjectMapper init() {
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
//		om.setDateFormat(df);
		return om;
	}
	public static void put(JsonNode node, String field, String value) {
		((ObjectNode) node).put(field, value);
	}
	public static <T> T createNode(String value, Class<T> type) throws IOException{
		return objectMapper.readValue(value, type);
	}
	public static String entityToJsonString(Object t) throws JsonProcessingException {
		return objectMapper.writeValueAsString(t);
	}
	public static ObjectNode createNewNode() {
		return objectMapper.createObjectNode();
	}
	public static JsonNode readTree(String json) throws IOException{
		return objectMapper.readTree(json);
	}
	public static <T> T readStream(InputStream stream, Class<T> type) throws IOException {
		return objectMapper.readValue(stream, type);
	}
	public static void setValueIfNotNull(JsonNode node, String propertieName,String value) {
		setValueIfNotNull((ObjectNode) node,propertieName, value);
	}
	private static void setValueIfNotNull(ObjectNode node, String propertieName,String value) {
		if(node==null|| value==null) {
			return;
		}
		node.put(propertieName, value);
	}
	public static String getString(Object node) throws JsonProcessingException {
		return objectMapper.writeValueAsString(node);
	}
	public static ObjectNode convertValue(Object node) {
		return objectMapper.convertValue(node, ObjectNode.class);
	}
	public static void removeElement (String key, JsonNode node) {
		((ObjectNode)node).remove(key);
	}
	public static void makeNullElementEmpty(JsonNode node){
		Iterator<Map.Entry<String,JsonNode>> fields = node.fields();
		while(fields !=null && fields.hasNext()) {
			Map.Entry<String,JsonNode> entry = fields.next();
			if(checkIfElementIsNull(entry.getValue())) {
				((ObjectNode)node).put(entry.getKey(), "");
			}
		}
	}
	private static boolean checkIfElementIsNull(JsonNode node) {
		if(node.isArray()) {
			if(checkIfArrayElementIsNull(node)||node.isNull()) {
				return true;
			}
		}else if(node.isObject()) {
			makeNullElementEmpty(node);
		}else if(node.isNull()) {
			return true;
		}
		return false;
	}
	private static boolean checkIfArrayElementIsNull(JsonNode arrayNode) {
		Iterator<JsonNode> arrayFields = arrayNode.elements();
		while(arrayFields.hasNext()) {
			JsonNode node = arrayFields.next();
			return checkIfElementIsNull(node);
		}
		return false;
	}
	public  static void removeEmptyElements(JsonNode node) {
		Iterator<JsonNode> fields = node.elements();
		while(fields.hasNext()) {
			JsonNode jsonNode = fields.next();
			removeEmptyElementsFromNonEmptyObject(fields, jsonNode);
		}
	}
	private static void removeEmptyElementsFromNonEmptyObject(Iterator<JsonNode> fields, JsonNode jsonNode) {
		if(jsonNode.isArray()) {
			removeEmptyElementsFromArray(jsonNode);
			if(jsonNode.isEmpty(null)) {
				fields.remove();
			}
		}else if (jsonNode.isObject()) {
			removeEmptyElements(jsonNode);
		}else if (jsonNode.isNull() || StringUtils.isBlank(jsonNode.asText())) {
			fields.remove();
		}
	}
	private static void removeEmptyElementsFromArray(JsonNode arrayNode) {
		Iterator<JsonNode> arrayFileds = arrayNode.elements();
		while(arrayFileds.hasNext()) {
			JsonNode node = arrayFileds.next();
			removeEmptyElementsFromNonEmptyObject(arrayFileds,node);
		}
	}
	public static String returnValueStringFromPropertie(String propertieName,JsonNode node) {
		if(node==null||node.isNull()){
			return null;
		}
		JsonNode jsonNode = node.get(propertieName);
		if (jsonNode == null||jsonNode.isNull())
			return null;
		else
			return jsonNode.asText();
	}
	public static <T> List<T> returnValueListFromPropertie(String propertiName, JsonNode node, Class<T> type) throws IOException{
		JsonNode jsonNode = node.get(propertiName);
		if(jsonNode == null || jsonNode.isNull()) {
			return null;
		}else {
			return objectMapper.readValue(jsonNode.toString(), objectMapper.getTypeFactory().constructCollectionLikeType(List.class,type));
		}
	}
	public static <T> T jsonStringToObject(String jsonString, Class<T> classType) throws IOException{
		return	objectMapper.readValue(jsonString, classType);
	}
	public static ArrayNode listObjectToJsonNode(List<?> list ) {
		return objectMapper.valueToTree(list);
	}
	public static String createJsonLine(String key, String value) throws JsonProcessingException {
		ObjectNode response = JackJsonUtils.createNewNode();
		JackJsonUtils.put(response, key,value);
		return JackJsonUtils.getString(response);
	}
	
}
