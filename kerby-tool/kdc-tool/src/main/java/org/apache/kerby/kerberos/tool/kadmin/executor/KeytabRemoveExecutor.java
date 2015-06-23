/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.kerby.kerberos.tool.kadmin.executor;

import org.apache.kerby.KOptions;
import org.apache.kerby.config.Config;
import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.admin.Kadmin;
import org.apache.kerby.kerberos.kerb.admin.KadminOption;
import org.apache.kerby.kerberos.tool.kadmin.tool.KadminTool;

import java.io.File;

public class KeytabRemoveExecutor implements KadminCommandExecutor{
    private static final String USAGE =
            "Usage: ktremove [-k[eytab] keytab] [-q] principal [kvno | all | old]";

    private static final String DEFAULT_KEYTAB_FILE_LOCATION = "/etc/krb5.keytab";

    private Config backendConfig;

    public KeytabRemoveExecutor(Config backendConfig) {
        this.backendConfig = backendConfig;
    }

    @Override
    public void execute(String input) {
        String[] commands = input.split("\\s+");
        if (commands.length < 2 || commands.length > 6) {
            System.err.println(USAGE);
            return;
        }

        String principal = null;
        String keytabFileLocation = null;
        String rangeSuffix = null;
        int lastIndex ;

        if (commands[commands.length - 1].matches("^all|old|-?\\d+$")) {
            if (commands.length < 3) {
                System.err.println(USAGE);
                return;
            }
            lastIndex = commands.length - 3;
            principal = commands[commands.length - 2];
            rangeSuffix = commands[commands.length - 1];
        } else {
            lastIndex = commands.length - 2;
            principal = commands[commands.length - 1];
        }
        KOptions kOptions = KadminTool.parseOptions(commands, 1, lastIndex);

        if (principal == null || kOptions == null ||
                kOptions.contains(KadminOption.K) && kOptions.contains(KadminOption.KEYTAB)) {
            System.err.println(USAGE);
            return;
        }

        keytabFileLocation = kOptions.contains(KadminOption.K)?
                kOptions.getStringOption(KadminOption.K):kOptions.getStringOption(KadminOption.KEYTAB);

        if (keytabFileLocation == null) {
            keytabFileLocation = DEFAULT_KEYTAB_FILE_LOCATION;
        }
        File keytabFile = new File(keytabFileLocation);

        Kadmin kadmin = new Kadmin(backendConfig);
        try {
            StringBuilder result = kadmin.removeEntryFromKeytab(keytabFile, principal, rangeSuffix);
            result.append("\tFile:" + keytabFileLocation);
            System.out.println(result.toString());
        } catch (KrbException e) {
            System.err.println("Principal \"" + principal + "\" fail to remove entry from keytab." +
                e.getMessage());
        }
    }
}