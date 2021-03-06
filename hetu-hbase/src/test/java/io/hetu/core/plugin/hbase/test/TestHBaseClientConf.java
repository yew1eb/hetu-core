/*
 * Copyright (C) 2018-2020. Huawei Technologies Co., Ltd. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hetu.core.plugin.hbase.test;

import io.hetu.core.plugin.hbase.conf.HBaseColumnProperties;
import io.hetu.core.plugin.hbase.conf.HBaseConfig;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;

/**
 * TestHBaseClientConf
 *
 * @since 2020-03-20
 */
public class TestHBaseClientConf
{
    /**
     * testHBaseClientConf
     */
    @Test
    public void testHBaseClientConf()
    {
        HBaseConfig hcc = new HBaseConfig();

        hcc.setZkQuorum("zk1");
        assertEquals("zk1", hcc.getZkQuorum());

        hcc.setPrincipalUsername("root");
        assertEquals("root", hcc.getPrincipalUsername());

        hcc.setZkClientPort("2181");
        assertEquals("2181", hcc.getZkClientPort());

        hcc.setMetastoreType("type");
        assertEquals("type", hcc.getMetastoreType());

        hcc.setMetastoreUrl("file");
        assertEquals("file", hcc.getMetastoreUrl());

        hcc.setKerberos("file");
        assertEquals("file", hcc.getKerberos());

        hcc.setRetryNumber(10);
        assertEquals(10, hcc.getRetryNumber());

        hcc.setPauseTime(10);
        assertEquals(10, hcc.getPauseTime());

        hcc.setRpcProtectionEnable(true);
        assertEquals(true, hcc.isRpcProtectionEnable());

        hcc.setJaasConfPath("/etc/hetu/");
        assertEquals("/etc/hetu/", hcc.getJaasConfPath());

        hcc.setCoreSitePath("/etc/hetu/");
        assertEquals("/etc/hetu/", hcc.getCoreSitePath());

        hcc.setHdfsSitePath("/etc/hetu/");
        assertEquals("/etc/hetu/", hcc.getHdfsSitePath());

        hcc.setHbaseSitePath("/etc/hetu/");
        assertEquals("/etc/hetu/", hcc.getHbaseSitePath());

        hcc.setKrb5ConfPath("/etc/hetu/");
        assertEquals("/etc/hetu/", hcc.getKrb5ConfPath());

        hcc.setUserKeytabPath("/etc/hetu/");
        assertEquals("/etc/hetu/", hcc.getUserKeytabPath());

        hcc.setDefaultValue("default");
        assertEquals("default", hcc.getDefaultValue());
    }

    /**
     * testHBaseColumnProperties
     */
    @Test
    public void testHBaseColumnProperties()
    {
        HBaseColumnProperties.getColumnProperties();
    }
}
