package com.fubukigrin.utilities;

public enum OsuGrades {

    XH("<:rankingXH:1231315541394722847>"),
    X("<:rankingX:1231315525141921924>"),
    SH("<:rankingSH:1231315518749806742>"),
    S("<:rankingS:1231315510612983880>"),
    A("<:rankingA:1231315423241441341>"),
    B("<:rankingB:1231315451234226316>"),
    C("<:rankingC:1231315461879365714>"),
    D("<:rankingD:1231315468346986506>");
    
    private final String grade;

    private OsuGrades(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return grade;
    }
}
