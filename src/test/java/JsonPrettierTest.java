import org.junit.Assert;
import org.junit.Test;

public class JsonPrettierTest {
    private static String getPrettifiedJson(String inLineJson) {
        return new JsonPrettier(inLineJson).prettify();
    }

    @Test
    public void empty_string_returns_empty() {
        String json = getPrettifiedJson("");
        assert json.isEmpty();
    }

    @Test
    public void empty_json_test() {
        String json = getPrettifiedJson("{}");
        assert json.equals("{}");

    }

    @Test
    public void key_string_value_int_json_test() {
        String json = getPrettifiedJson("{\"key\":12}");
        Assert.assertEquals("{\n\t\"key\": 12\n}", json);
    }

    @Test
    public void key_string_value_string_json_test() {
        String json = getPrettifiedJson("{\"key\":\"12\"}");
        Assert.assertEquals("{\n\t\"key\": \"12\"\n}", json);
    }

    @Test
    public void key_string_value_float_json_test() {
        String json = getPrettifiedJson("{\"key\":12.23}");
        Assert.assertEquals("{\n\t\"key\": 12.23\n}", json);
    }

    @Test
    public void more_than_one_row_json_test() {
        String json = getPrettifiedJson("{\"key\":12.23, \"name\" : \"ali\"}");
        Assert.assertEquals("{\n\t\"key\": 12.23,\n\t\"name\": \"ali\"\n}", json);
    }

    @Test(expected = RuntimeException.class)
    public void parse_wrong_json_must_throw_exception(){
        String json = getPrettifiedJson("{\"name\":  ali}");
    }

    @Test()
    public void one_nested_json_test(){
        String json = getPrettifiedJson("{\"car\" : {\"name\":\"lexux\", \"model\":12}}");
        Assert.assertEquals("{\n\t\"car\": {\n\t\t\"name\": \"lexux\",\n\t\t\"model\": 12\n\t}\n}",json);
    }



}
