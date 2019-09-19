package be.ordina.jworks.helm.house;

public class MotionStatus {

    private final String question;
    private final Long ayes;
    private final Long noes;

    public MotionStatus(Motion motion){
        this.question = motion.getQuestion();
        this.ayes = motion.getAyes();
        this.noes = motion.getNoes();
    }

    private MotionStatus(){
        this.question = "";
        this.ayes = 0L;
        this.noes = 0L;
    }

    public static MotionStatus empty(){
        return new MotionStatus();
    }

    public String getQuestion() {
        return question;
    }

    public Long getAyes() {
        return ayes;
    }

    public Long getNoes() {
        return noes;
    }
}
