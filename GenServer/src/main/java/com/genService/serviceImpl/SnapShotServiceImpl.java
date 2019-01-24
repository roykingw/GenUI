package com.genService.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.genService.mapper.GrsnapshotLogMapper;
import pojo.GrsnapshotLog;
import pojo.GrsnapshotLogExample;
import pojo.GrsnapshotLogWithBLOBs;
import services.SnapShotService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SnapShotServiceImpl extends BaseServiceImpl<GrsnapshotLog> implements SnapShotService  {

	@Resource
	private GrsnapshotLogMapper snapShotMapper;
	
	public int save(GrsnapshotLogWithBLOBs snapShotData) {
		return snapShotMapper.insertSelective(snapShotData);
	}

	public GrsnapshotLogWithBLOBs getSnapShot(String snapshotId) {
		GrsnapshotLogWithBLOBs res = new GrsnapshotLogWithBLOBs();
		GrsnapshotLogExample example = new GrsnapshotLogExample();
		GrsnapshotLogExample.Criteria criteria  = example.createCriteria();
		criteria.andIdEqualTo(snapshotId);
		List<GrsnapshotLogWithBLOBs> queryRes = snapShotMapper.selectByExampleWithBLOBs(example);
		if(queryRes.size()==1){
			res = queryRes.get(0);
		}
		return res;
	}

}
