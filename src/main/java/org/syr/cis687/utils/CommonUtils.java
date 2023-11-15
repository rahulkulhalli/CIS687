package org.syr.cis687.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.syr.cis687.enums.OpType;
import org.syr.cis687.enums.ResponseType;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.Shuttle;
import org.syr.cis687.models.Student;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CommonUtils {
    public static <T> List<T> convertIterableToList(Iterable<T> obj) {
        List<T> list = new ArrayList<>();
        for (T x: obj) {
            list.add(x);
        }

        return list;
    }

    private static <T> ResponseEntity<ApiResponse> getFilledApiResponse(T obj, ResponseType responseType) {
        ApiResponse response = new ApiResponse();
        // obj can be null.
        response.setData(obj);

        switch (responseType) {
            case NULL_OBJ -> {
                response.setStatusCode(HttpStatus.NO_CONTENT.value());
                response.setStatus("Could not process null Object");
                response.setError(HttpStatus.NO_CONTENT.getReasonPhrase());
                return ResponseEntity.badRequest().body(response);
            }
            case CREATED -> {
                response.setStatusCode(HttpStatus.CREATED.value());
                response.setStatus("Successfully inserted into DB.");
                response.setError(null);
                return ResponseEntity.ok(response);
            }
            case NOT_CREATED -> {
                response.setStatusCode(HttpStatus.NOT_IMPLEMENTED.value());
                response.setStatus("Could not insert into DB.");
                response.setError(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase());
                return ResponseEntity.badRequest().body(response);
            }
            case UPDATED -> {
                response.setStatusCode(HttpStatus.OK.value());
                response.setStatus("Successfully updated entry in DB.");
                response.setError(null);
                return ResponseEntity.ok(response);
            }
            case NOT_UPDATED -> {
                response.setStatusCode(HttpStatus.NOT_MODIFIED.value());
                response.setStatus("Could not update entry in DB.");
                response.setError(HttpStatus.NOT_MODIFIED.getReasonPhrase());
                return ResponseEntity.badRequest().body(response);
            }
            case DELETED -> {
                response.setStatusCode(HttpStatus.OK.value());
                response.setStatus("Successfully deleted entry from DB.");
                response.setError(null);
                return ResponseEntity.ok(response);
            }
            case NOT_DELETED -> {
                response.setStatusCode(HttpStatus.NOT_MODIFIED.value());
                response.setStatus("Could not delete entry from DB.");
                response.setError(HttpStatus.NOT_MODIFIED.getReasonPhrase());
                return ResponseEntity.badRequest().body(response);
            }
            case FOUND -> {
                response.setStatusCode(HttpStatus.OK.value());
                response.setStatus("Found entry in DB.");
                response.setError(null);
                return ResponseEntity.ok(response);
            }
            case NOT_FOUND -> {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setStatus("Could not find entry in DB.");
                response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
                return ResponseEntity.badRequest().body(response);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * A method to validate the body against a given OpType.
     * @param obj Object to validate.
     * @param opType Operation type to validate.
     * @return API Response.
     * @param <T> Generic body.
     */
    @SuppressWarnings("rawtypes")
    public static <T> ResponseEntity<ApiResponse> validateAndReturn(T obj, OpType opType) {
        try {
            // Check to see if object is null.
            if (obj == null) {
                return getFilledApiResponse(null, ResponseType.NULL_OBJ);
            }

            switch (opType) {
                case INSERT -> {
                    return getFilledApiResponse(obj, ResponseType.CREATED);
                }
                case FIND_ALL -> {
                    if (((List) obj).isEmpty()) {
                        return getFilledApiResponse(obj, ResponseType.NOT_FOUND);
                    }
                    return getFilledApiResponse(obj, ResponseType.FOUND);
                }
                case FIND_ONE -> {
                    // We know that if find_one reaches here, it is successful.
                    return getFilledApiResponse(obj, ResponseType.FOUND);
                }
                case DELETE -> {
                    if (!(Boolean) obj) {
                        return getFilledApiResponse(false, ResponseType.NOT_DELETED);
                    }
                    return getFilledApiResponse(true, ResponseType.DELETED);
                }
                default -> {
                    return getFilledApiResponse(obj, ResponseType.UPDATED);
                }
            }

        } catch (Exception e) {
            // Return a Server error response.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a pre-formatted Bad response.
     * @param data Data body
     * @param errorMsg A custom message to return.
     * @return The Spring API response.
     * @param <T> Generic return type.
     */
    public static <T> ResponseEntity<ApiResponse> getBadResponse(T data, String errorMsg) {
        ApiResponse badResponse = new ApiResponse();
        badResponse.setData(data);
        badResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        badResponse.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        badResponse.setError(errorMsg);

        return ResponseEntity.badRequest().body(badResponse);
    }

    /**
     * Get a pre-formatted OK response.
     * @param data Data body
     * @param successMsg A custom message to return.
     * @return The Spring API response.
     * @param <T> Generic return type.
     */
    public static <T> ResponseEntity<ApiResponse> getOkResponse(T data, String successMsg) {
        ApiResponse badResponse = new ApiResponse();
        badResponse.setData(data);
        badResponse.setStatusCode(HttpStatus.OK.value());
        badResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        badResponse.setError(successMsg);

        return ResponseEntity.ok().body(badResponse);
    }

    /**
     * Check if the student is on the shuttle.
     * @param shuttle Current shuttle.
     * @param studentId StudentID to search.
     * @return A boolean value indicating whether the student is on the shuttle.
     */
    public static boolean isStudentOnShuttle(Shuttle shuttle, String studentId) {
        try {
            for (Student s : shuttle.getPassengerList()) {
                if (s.getOrgId().trim().equalsIgnoreCase(studentId.trim())) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the index of the current student in the shuttle. Return -1 otherwise.
     * @param shuttle Shuttle object
     * @param studentId Student to search for.
     * @return Index of student in the shuttle.
     */
    public static int getStudentIndex(Shuttle shuttle, String studentId) {

        if (!isStudentOnShuttle(shuttle, studentId)) {
            return -1;
        }

        try {
            for (int i = 0; i < shuttle.getPassengerList().size(); i++) {
                Student student = shuttle.getPassengerList().get(i);
                if (student.getOrgId().trim().equalsIgnoreCase(studentId.trim())) {
                    return i;
                }
            }

            return -1;
        } catch (Exception e) {
            return -1;
        }
    }
}
