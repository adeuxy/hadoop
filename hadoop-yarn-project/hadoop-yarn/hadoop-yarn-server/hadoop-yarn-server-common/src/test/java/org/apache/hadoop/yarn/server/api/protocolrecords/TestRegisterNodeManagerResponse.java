/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.yarn.server.api.protocolrecords;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import org.apache.hadoop.yarn.factories.RecordFactory;
import org.apache.hadoop.yarn.factory.providers.RecordFactoryProvider;
import org.apache.hadoop.yarn.server.api.protocolrecords.impl.pb.RegisterNodeManagerResponsePBImpl;
import org.apache.hadoop.yarn.server.api.records.MasterKey;
import org.apache.hadoop.yarn.server.api.records.NodeAction;
import org.junit.Test;

import org.apache.hadoop.yarn.proto.YarnServerCommonServiceProtos.RegisterNodeManagerResponseProto;

public class TestRegisterNodeManagerResponse {
  private static final RecordFactory recordFactory = 
    RecordFactoryProvider.getRecordFactory(null);
  
  @Test
  public void testRoundTrip() throws Exception {
    RegisterNodeManagerResponse resp = recordFactory
    .newRecordInstance(RegisterNodeManagerResponse.class);
    MasterKey mk = recordFactory.newRecordInstance(MasterKey.class);
    mk.setKeyId(54321);
    byte b [] = {0,1,2,3,4,5};
    mk.setBytes(ByteBuffer.wrap(b));
    resp.setMasterKey(mk);
    resp.setNodeAction(NodeAction.NORMAL);
    
    assertEquals(NodeAction.NORMAL, resp.getNodeAction());
    assertNotNull(resp.getMasterKey());
    assertEquals(54321, resp.getMasterKey().getKeyId());
    assertArrayEquals(b, resp.getMasterKey().getBytes().array());
    
    RegisterNodeManagerResponse respCopy = serDe(resp);
    
    assertEquals(NodeAction.NORMAL, respCopy.getNodeAction());
    assertNotNull(respCopy.getMasterKey());
    assertEquals(54321, respCopy.getMasterKey().getKeyId());
    assertArrayEquals(b, respCopy.getMasterKey().getBytes().array());
  }

  public static RegisterNodeManagerResponse serDe(RegisterNodeManagerResponse orig) throws Exception {
    RegisterNodeManagerResponsePBImpl asPB = (RegisterNodeManagerResponsePBImpl)orig;
    RegisterNodeManagerResponseProto proto = asPB.getProto();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    proto.writeTo(out);
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    RegisterNodeManagerResponseProto.Builder cp = RegisterNodeManagerResponseProto.newBuilder();
    cp.mergeFrom(in);
    return new RegisterNodeManagerResponsePBImpl(cp.build());
  }

}
