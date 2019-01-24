package services;

/**
 * Created by roykingw on 2017/12/14 0014.
 */
public interface CboardService {
    public Object getBoardData(Long id) ;

    public Object getDatasetList();

    public String[] getDimensionValues(Long datasourceId, String query, Long datasetId, String colmunName, String cfg, Boolean reload) ;

    public Object getTestInfo() ;

    public Object getAggregateData(Long datasourceId, String query, Long datasetId, String cfg, Boolean reload) ;

//    public Object tableToxls(String data) ;

    public byte[] exportBoard(Long id) throws Exception;
}
