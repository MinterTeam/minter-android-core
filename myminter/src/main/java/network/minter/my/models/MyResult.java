package network.minter.my.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MyResult<Result> {
    public Result data;
    public Error error;
    public Meta meta;
    public Links links;

    public boolean isSuccess() {
        return error == null;
    }

    public Links getLinks() {
        if (links == null) {
            links = new Links();
        }

        return links;
    }

    public Meta getMeta() {
        if (meta == null) {
            meta = new Meta();
        }

        return meta;
    }

    public Error getError() {
        if (error == null) {
            error = new Error();
        }

        return error;
    }

    public static class Meta {
        public int total;
        public int count;
        public int perPage;
        public int currentPage;
        public int firstPage;
        public int lastPage;
    }

    public static class Links {
        public String prev;
        public String next;
        public String first;
        public String last;
    }

    public static class Error {
        public String code;
        public String message;

        /**
         * for example, validation errors:
         * email: {
         * "invalid email format",
         * "can't be empty"
         * "et cetera"
         * },
         * "some": {
         * "another field error"
         * }
         */
        public Map<String, List<String>> data;

        public Map<String, List<String>> getData() {
            if (data == null) {
                return Collections.emptyMap();
            }

            return data;
        }
    }
}
