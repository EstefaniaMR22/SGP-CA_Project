package model.domain;

public class Participation {
    private Member member;
    private ParticipationType participationType;

    public Participation() {
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public ParticipationType getParticipationType() {
        return participationType;
    }

    public void setParticipationType(ParticipationType participationType) {
        this.participationType = participationType;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "member=" + member +
                ", participationType=" + participationType +
                '}';
    }
}
