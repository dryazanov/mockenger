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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Dmitry Ryazanov on 6/18/2015.
 */
public class CommonUtilsTest {

    private final PostRequest postTestRequest = new PostRequest();

    private static final String JSON1 = "{\"valid\":\"ok\",\"mock\":\"4\"}";

    private static final String URL1 = "/service/test/1";

    private final Set<Pair> goodParameters = new HashSet<>();
    private static final String PARAM_NAME1 = "a";
    private static final String PARAM_NAME2 = "b";
    private static final String PARAM_VALUE1 = "1";
    private static final String PARAM_VALUE2 = "2";

    private final static String RESULT1 = "7794d3d9da59aec955a4b29c7f4157e1";


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
        assertEquals("5ba819f4b811edef9082b1fab8d36150", CommonUtils.generateCheckSum(postTestRequest));
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
    public void testAllMapsNotEmptyTrue() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key", "value");

        Map<String, String> map2 = new HashMap<>();
        map2.put("key", "value");

        assertTrue(CommonUtils.allNotEmpty(map1, map2));
    }

    @Test
    public void testAllSetsNotEmptyTrue() {
        Set<Pair> set1 = new HashSet<>();
        set1.add(new Pair("key", "value"));

        Set<Pair> set2 = new HashSet<>();
        set2.add(new Pair("key", "value"));

        assertTrue(CommonUtils.allNotEmpty(set1, set2));
    }

    @Test
    public void testAllMapsNotEmptyFalse() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        assertFalse(CommonUtils.allNotEmpty(Collections.emptyMap(), map));
    }

    @Test
    public void testAllSetsNotEmptyFalse() {
        Set<Pair> set1 = new HashSet<>();
        set1.add(new Pair("key", "value"));

        assertFalse(CommonUtils.allNotEmpty(Collections.emptySet(), set1));
    }

    @Test
    public void testContainsAllInMapTrue() {
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
    public void testContainsAllInSetTrue() {
        Set<Pair> set1 = new HashSet<>();
        set1.add(new Pair("key1", "value1"));
        set1.add(new Pair("key2", "value2"));
        set1.add(new Pair("key3", "value3"));

        Set<Pair> set2 = new HashSet<>();
        set2.add(new Pair("key1", "value1"));
        set2.add(new Pair("key3", "value3"));

        assertTrue(CommonUtils.containsAll(set1, set2));
    }

    @Test
    public void testContainsAllInMapFalse() {
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
    public void testContainsAllInSetFalse() {
        Set<Pair> set1 = new HashSet<>();
        set1.add(new Pair("key1", "value1"));
        set1.add(new Pair("key2", "value2"));
        set1.add(new Pair("key3", "value3"));

        Set<Pair> set2 = new HashSet<>();
        set2.add(new Pair("key1", "value1"));
        set2.add(new Pair("key4", "value4"));

        assertFalse(CommonUtils.containsAll(set1, set2));
    }

    @Test
    public void testEqualEntriesInMapTrue() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key2", "value2");
        map1.put("key3", "value3");
        map1.put("key1", "value1");

        Map<String, String> map2 = new HashMap<>();
        map2.put("key1", "value1");
        map2.put("key3", "value3");
        map2.put("key2", "value2");

        assertTrue(CommonUtils.containsEqualEntries(map1, map2));
    }

    @Test
    public void testEqualEntriesInSetTrue() {
        Set<Pair> set1 = new HashSet<>();
        set1.add(new Pair("key2", "value2"));
        set1.add(new Pair("key3", "value3"));
        set1.add(new Pair("key1", "value1"));

        Set<Pair> set2 = new HashSet<>();
        set2.add(new Pair("key1", "value1"));
        set2.add(new Pair("key3", "value3"));
        set2.add(new Pair("key2", "value2"));

        assertTrue(CommonUtils.containsEqualEntries(set1, set2));
    }

    @Test
    public void testEqualEntriesInMapFalse() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key2", "value2");
        map1.put("key1", "value1");
        map1.put("key3", "value3");

        Map<String, String> map2 = new HashMap<>();
        map2.put("key1", "value1");
        map2.put("key4", "value4");
        map2.put("key2", "value2");

        assertFalse(CommonUtils.containsEqualEntries(map1, map2));
    }

    @Test
    public void testEqualEntriesInSetFalse() {
        Set<Pair> set1 = new HashSet<>();
        set1.add(new Pair("key2", "value2"));
        set1.add(new Pair("key1", "value1"));
        set1.add(new Pair("key3", "value3"));

        Set<Pair> set2 = new HashSet<>();
        set2.add(new Pair("key1", "value1"));
        set2.add(new Pair("key4", "value4"));
        set2.add(new Pair("key2", "value2"));

        assertFalse(CommonUtils.containsEqualEntries(set1, set2));
    }

    @Test
    public void testEqualEntriesEmptyMap() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");

        assertFalse(CommonUtils.containsEqualEntries(map1, Collections.emptyMap()));
    }

    @Test
    public void testEqualEntriesEmptySet() {
        Set<Pair> set1 = new HashSet<>();
        set1.add(new Pair("key2", "value2"));

        assertFalse(CommonUtils.containsEqualEntries(set1, Collections.emptySet()));
    }
}
