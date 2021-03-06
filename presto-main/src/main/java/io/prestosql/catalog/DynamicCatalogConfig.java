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

package io.prestosql.catalog;

import io.airlift.configuration.Config;
import io.airlift.configuration.ConfigDescription;
import io.airlift.configuration.LegacyConfig;
import io.airlift.units.DataSize;
import io.airlift.units.Duration;
import io.airlift.units.MaxDataSize;
import io.airlift.units.MinDuration;

import static io.airlift.units.DataSize.Unit.KILOBYTE;

public class DynamicCatalogConfig
{
    private boolean dynamicCatalogEnabled;
    private String catalogConfigurationDir;
    private String catalogShareConfigurationDir;
    private Duration catalogScannerInterval = Duration.valueOf("5s");
    private DataSize catalogMaxFileSize = new DataSize(128, KILOBYTE);

    @Config("catalog.dynamic-enabled")
    @ConfigDescription("Whether to enable dynamic catalog.")
    public DynamicCatalogConfig setDynamicCatalogEnabled(boolean dynamicCatalogEnabled)
    {
        this.dynamicCatalogEnabled = dynamicCatalogEnabled;
        return this;
    }

    public boolean isDynamicCatalogEnabled()
    {
        return this.dynamicCatalogEnabled;
    }

    @LegacyConfig("catalog.config-dir")
    @Config("catalog.local.config-dir")
    @ConfigDescription("Root directory for storing configuration files in local disk.")
    public DynamicCatalogConfig setCatalogConfigurationDir(String dir)
    {
        this.catalogConfigurationDir = dir;
        return this;
    }

    public String getCatalogConfigurationDir()
    {
        return catalogConfigurationDir;
    }

    @Config("catalog.share.config-dir")
    @ConfigDescription("Root directory for storing configuration files in the shared file system.")
    public DynamicCatalogConfig setCatalogShareConfigurationDir(String dir)
    {
        this.catalogShareConfigurationDir = dir;
        return this;
    }

    public String getCatalogShareConfigurationDir()
    {
        return catalogShareConfigurationDir;
    }

    @Config("catalog.scanner-interval")
    @ConfigDescription("Interval for scanning catalogs in the shared file system, default value is 5s.")
    @MinDuration("5s")
    public DynamicCatalogConfig setCatalogScannerInterval(Duration catalogScannerInterval)
    {
        this.catalogScannerInterval = catalogScannerInterval;
        return this;
    }

    public Duration getCatalogScannerInterval()
    {
        return catalogScannerInterval;
    }

    @Config("catalog.max-file-size")
    @ConfigDescription("Maximum file size, default value is 128k.")
    @MaxDataSize("10MB")
    public DynamicCatalogConfig setCatalogMaxFileSize(DataSize catalogMaxFileSize)
    {
        this.catalogMaxFileSize = catalogMaxFileSize;
        return this;
    }

    public DataSize getCatalogMaxFileSize()
    {
        return catalogMaxFileSize;
    }
}
