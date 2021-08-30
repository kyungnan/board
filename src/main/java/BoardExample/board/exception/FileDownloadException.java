package BoardExample.board.exception;

public class FileDownloadException extends RuntimeException{

    public FileDownloadException(String msg){
        super(msg);
    }

    public FileDownloadException(String msg, Throwable caues){
        super(msg, caues);
    }
}
