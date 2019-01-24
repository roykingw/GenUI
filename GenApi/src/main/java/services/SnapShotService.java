package services;

import pojo.GrsnapshotLog;
import pojo.GrsnapshotLogWithBLOBs;

/**
 * Created by roykingw on 2017/12/18 0018.
 */
public interface SnapShotService extends BaseService<GrsnapshotLog>{

    public int save(GrsnapshotLogWithBLOBs snapShotData);

    public GrsnapshotLogWithBLOBs getSnapShot(String snapshotId);
}
