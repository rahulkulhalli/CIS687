package org.syr.cis687.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.syr.cis687.enums.OpType;
import org.syr.cis687.enums.ResponseType;
import org.syr.cis687.models.ApiResponse;

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
                response.setStatus(HttpStatus.NO_CONTENT.getReasonPhrase());
                response.setError("Could not process null Object");
                return ResponseEntity.badRequest().body(response);
            }
            case CREATED -> {
                response.setStatusCode(HttpStatus.CREATED.value());
                response.setStatus(HttpStatus.OK.getReasonPhrase());
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
                response.setStatus("Could not delete entry from DB.");
                response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
                return ResponseEntity.badRequest().body(response);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

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
}
