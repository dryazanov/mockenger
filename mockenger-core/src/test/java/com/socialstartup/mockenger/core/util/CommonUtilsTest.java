package com.socialstartup.mockenger.core.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PostRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
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

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String KEY1 = "key1";
    private static final String VALUE1 = "value1";
    private static final String KEY2 = "key2";
    private static final String VALUE2 = "value2";
    private static final String KEY3 = "key3";
    private static final String VALUE3 = "value3";
    private static final String KEY4 = "key4";
    private static final String VALUE4 = "value4";

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
        assertFalse(CommonUtils.allEmpty(Collections.emptyMap(), newMap(0)));
    }

    @Test
    public void testAllMapsNotEmptyTrue() {
        assertTrue(CommonUtils.allNotEmpty(newMap(0), newMap(0)));
    }

    @Test
    public void testAllSetsNotEmptyTrue() {
        assertTrue(CommonUtils.allNotEmpty(newSet(0), newSet(0)));
    }

    @Test
    public void testAllMapsNotEmptyFalse() {
        assertFalse(CommonUtils.allNotEmpty(Collections.emptyMap(), newMap(0)));
    }

    @Test
    public void testAllSetsNotEmptyFalse() {
        assertFalse(CommonUtils.allNotEmpty(Collections.emptySet(), newSet(0)));
    }

    @Test
    public void testContainsAllInMapTrue() {
        assertTrue(CommonUtils.containsAll(newMap(1, 2, 3), newMap(1, 3)));
    }

    @Test
    public void testContainsAllInSetTrue() {
        assertTrue(CommonUtils.containsAll(newSet(1, 2, 3), newSet(1, 3)));
    }

    @Test
    public void testContainsAllInMapFalse() {
        assertFalse(CommonUtils.containsAll(newMap(1, 2, 3), newMap(1, 4)));
    }

    @Test
    public void testContainsAllInSetFalse() {
        assertFalse(CommonUtils.containsAll(newSet(1, 2, 3), newSet(1, 4)));
    }

    @Test
    public void testEqualEntriesInMapTrue() {
        assertTrue(CommonUtils.containsEqualEntries(newMap(2, 3, 1), newMap(1, 3, 2)));
    }

    @Test
    public void testEqualEntriesInSetTrue() {
        assertTrue(CommonUtils.containsEqualEntries(newSet(2, 3, 1), newSet(1, 3, 2)));
    }

    @Test
    public void testEqualEntriesInMapFalse() {
        assertFalse(CommonUtils.containsEqualEntries(newMap(2, 1, 3), newMap(1, 4, 2)));
    }

    @Test
    public void testEqualEntriesInSetFalse() {
        assertFalse(CommonUtils.containsEqualEntries(newSet(2, 1, 3), newSet(1, 4, 2)));
    }

    @Test
    public void testEqualEntriesEmptyMap() {
        assertFalse(CommonUtils.containsEqualEntries(newMap(1), Collections.emptyMap()));
    }

    @Test
    public void testEqualEntriesMapWithNull() {
        Map<String, String> map1 = null, map2 = null;
        assertFalse(CommonUtils.containsEqualEntries(map1, map2));
        assertFalse(CommonUtils.containsEqualEntries(map1, newMap(1)));
        assertFalse(CommonUtils.containsEqualEntries(newMap(1), map2));
    }

    @Test
    public void testEqualEntriesEmptySet() {
        assertFalse(CommonUtils.containsEqualEntries(newSet(2), Collections.emptySet()));
    }

    @Test
    public void testEqualEntriesSetWithNull() {
        Set<Pair> set1 = null, set2 = null;
        assertFalse(CommonUtils.containsEqualEntries(set1, set2));
        assertFalse(CommonUtils.containsEqualEntries(set1, newSet(1)));
        assertFalse(CommonUtils.containsEqualEntries(newSet(1), set2));
    }

    private Pair getPair(String key, String value) {
        return new Pair(key, value);
    }

    private Set<Pair> newSet(final int... numbers) {
        final ImmutableSet.Builder builder = ImmutableSet.builder();

        for (int num : numbers) {
            switch (num) {
                case 0: builder.add(getPair(KEY, VALUE));
                    continue;

                case 1: builder.add(getPair(KEY1, VALUE1));
                    continue;

                case 2: builder.add(getPair(KEY2, VALUE2));
                    continue;

                case 3: builder.add(getPair(KEY3, VALUE3));
                    continue;

                case 4: builder.add(getPair(KEY4, VALUE4));
            }
        }

        return builder.build();
    }

    private Map<String, String> newMap(final int... numbers) {
        final ImmutableMap.Builder builder = ImmutableMap.builder();

        for (int num : numbers) {
            switch (num) {
                case 0: builder.put(KEY, VALUE);
                    continue;

                case 1: builder.put(KEY1, VALUE1);
                    continue;

                case 2: builder.put(KEY2, VALUE2);
                    continue;

                case 3: builder.put(KEY3, VALUE3);
                    continue;

                case 4: builder.put(KEY4, VALUE4);
            }
        }

        return builder.build();
    }
}
