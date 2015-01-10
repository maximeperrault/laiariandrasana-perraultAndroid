package info.androidhive.actionbar;


        import org.json.JSONArray;

public class JSONParser {

    private String stg = "tmp";
    private JSONArray BeerArray = null;
    private JSONArray Beer = null;

    public void Input(String str){
        stg = str;
    }
    public String GetString(){
        return stg;
    }
    public void InputList(JSONArray Array){
        BeerArray = Array;
    }
    public JSONArray GetBeerArray(){ return BeerArray; }

    public void InputElements(JSONArray Array){
        Beer = Array;
    }
    public JSONArray GetBeer(){ return Beer; }

}