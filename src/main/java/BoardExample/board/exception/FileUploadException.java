package BoardExample.board.exception;

public class FileUploadException extends RuntimeException{

    public FileUploadException(String msg){
        super(msg);
    }

    public FileUploadException(String msg, Throwable caues){
        super(msg, caues);
    }
}
