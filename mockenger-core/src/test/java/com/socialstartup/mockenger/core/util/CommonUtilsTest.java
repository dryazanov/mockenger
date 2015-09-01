package com.socialstartup.mockenger.core.util;

import com.socialstartup.mockenger.data.model.persistent.mock.request.PostRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by x079089 on 6/18/2015.
 */
public class CommonUtilsTest {

    private final PostRequest postTestRequest = new PostRequest();

    private static final String JSON1 = "{\"valid\":\"ok\",\"mock\":\"4\"}";

    private static final String URL1 = "/service/test/1";

    private final SortedSet<Pair> goodParameters = new TreeSet<>();
    private static final String PARAM_NAME1 = "a";
    private static final String PARAM_NAME2 = "b";
    private static final String PARAM_VALUE1 = "1";
    private static final String PARAM_VALUE2 = "2";

    private final static String RESULT1 = "eeb4b7ef4faf4572e7138306a6a742b1";


    @Before
    public void init() {
        goodParameters.add(new Pair(PARAM_NAME1, PARAM_VALUE1));
        goodParameters.add(new Pair(PARAM_NAME2, PARAM_VALUE2));

        // Simulate request from user
        postTestRequest.setPath(new Path(URL1));
        postTestRequest.setParameters(new Parameters(goodParameters));
        postTestRequest.setBody(new Body(JSON1));
    }

    @Test
     public void testGetCheckSumOk() {
        assertEquals("ab032c860024bd86e9295a397a0964b9", CommonUtils.getCheckSum(postTestRequest));
    }

    @Test
    public void testGetCheckSumBodyNull() {
        postTestRequest.setBody(null);
        assertEquals(RESULT1, CommonUtils.getCheckSum(postTestRequest));
    }

    @Test
    public void testGetCheckSumBodyEmpty() {
        postTestRequest.setBody(new Body(""));
        assertEquals(RESULT1, CommonUtils.getCheckSum(postTestRequest));
    }

    @Test
    public void testGetCheckSumBodyEmptyParamsWithDifferentOrder() {
        goodParameters.add(new Pair(PARAM_NAME2, PARAM_VALUE2));
        goodParameters.add(new Pair(PARAM_NAME1, PARAM_VALUE1));
        postTestRequest.setParameters(new Parameters(goodParameters));
        postTestRequest.setBody(new Body(""));
        assertEquals(RESULT1, CommonUtils.getCheckSum(postTestRequest));
    }

    @Test
    public void testGenerateGetCheckSumOk() {
        assertEquals(RESULT1, CommonUtils.generateCheckSum(postTestRequest));
    }

    @Test
    public void testGenerateGetCheckSumPathNull() {
        postTestRequest.setPath(new Path(null));
        assertEquals("2bdfee688d37ba44d7fd300719161c6d", CommonUtils.generateCheckSum(postTestRequest));
    }

    @Test
     public void testGenerateGetCheckSumParametersNull() {
        postTestRequest.setParameters(new Parameters(null));
        assertEquals("45da51660e4a8d37a3b088282112c8f8", CommonUtils.generateCheckSum(postTestRequest));
    }

    @Test
    public void testGenerateGetCheckSumArgs() {
        StringBuilder sb = new StringBuilder();
        for (Pair pair : postTestRequest.getParameters().getValues()) {
            sb.append(pair.getKey()).append(pair.getValue());
        }
        String path = postTestRequest.getPath().getValue();
        String method = postTestRequest.getMethod().toString();
        assertEquals(RESULT1, CommonUtils.generateCheckSum(path, sb.toString(), method));
    }

    @Test
    public void testAllEmptyTrue() {
        assertTrue(CommonUtils.allEmpty(Collections.emptyMap(), Collections.emptyMap()));
    }

    @Test
    public void testAllEmptyFalse() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        assertFalse(CommonUtils.allEmpty(Collections.emptyMap(), map));
    }

    @Test
    public void testAllNotEmptyTrue() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key", "value");

        Map<String, String> map2 = new HashMap<>();
        map2.put("key", "value");

        assertTrue(CommonUtils.allNotEmpty(map1, map2));
    }

    @Test
    public void testAllNotEmptyFalse() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        assertFalse(CommonUtils.allNotEmpty(Collections.emptyMap(), map));
    }

    @Test
    public void testContainsAllTrue() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");
        map1.put("key3", "value3");

        Map<String, String> map2 = new HashMap<>();
        map2.put("key1", "value1");
        map2.put("key3", "value3");

        assertTrue(CommonUtils.containsAll(map1, map2));
    }

    @Test
    public void testContainsAllFalse() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");
        map1.put("key3", "value3");

        Map<String, String> map2 = new HashMap<>();
        map2.put("key1", "value1");
        map2.put("key4", "value4");

        assertFalse(CommonUtils.containsAll(map1, map2));
    }

    @Test
    public void testEqualEntriesTrue() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");
        map1.put("key3", "value3");

        Map<String, String> map2 = new HashMap<>();
        map2.put("key1", "value1");
        map2.put("key2", "value2");
        map2.put("key3", "value3");

        assertTrue(CommonUtils.containsEqualEntries(map1, map2));
    }

    @Test
    public void testEqualEntriesFalse() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");
        map1.put("key3", "value3");

        Map<String, String> map2 = new HashMap<>();
        map2.put("key1", "value1");
        map2.put("key2", "value2");
        map2.put("key4", "value4");

        assertFalse(CommonUtils.containsEqualEntries(map1, map2));
    }

    @Test
    public void testEqualEntriesEmptyMap() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");

        assertFalse(CommonUtils.containsEqualEntries(map1, Collections.emptyMap()));
    }
}
