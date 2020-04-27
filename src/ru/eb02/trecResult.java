package ru.eb02;

/**
 * trecResult holds ALL the values obtained from running a search hits file against TRECEval.
 */
public class trecResult {
  private final String runid;
  private final int num_q;
  private final int num_ret;
  private final int num_rel;
  private final int num_rel_ret;
  private final double map;
  private final double gm_map;
  private final double Rprec;
  private final double bpref;
  private final double recip_rank;
  private final double iprec_at_recall_000;
  private final double iprec_at_recall_010;
  private final double iprec_at_recall_020;
  private final double iprec_at_recall_030;
  private final double iprec_at_recall_040;
  private final double iprec_at_recall_050;
  private final double iprec_at_recall_060;
  private final double iprec_at_recall_070;
  private final double iprec_at_recall_080;
  private final double iprec_at_recall_090;
  private final double iprec_at_recall_100;
  private final double P_5;
  private final double P_10;
  private final double P_15;
  private final double P_20;
  private final double P_30;
  private final double P_100;
  private final double P_200;
  private final double P_500;
  private final double P_1000;
  private final boolean original;

  public trecResult (String runid,
                    int num_q,
                    int num_ret,
                    int num_rel,
                    int num_rel_ret,
                    double map,
                    double gm_map,
                    double Rprec,
                    double bpref,
                    double recip_rank,
                    double iprec_at_recall_000,
                    double iprec_at_recall_010,
                    double iprec_at_recall_020,
                    double iprec_at_recall_030,
                    double iprec_at_recall_040,
                    double iprec_at_recall_050,
                    double iprec_at_recall_060,
                    double iprec_at_recall_070,
                    double iprec_at_recall_080,
                    double iprec_at_recall_090,
                    double iprec_at_recall_100,
                    double P_5,
                    double P_10,
                    double P_15,
                    double P_20,
                    double P_30,
                    double P_100,
                    double P_200,
                    double P_500,
                    double P_1000,
                    boolean original) {
    this.runid = runid;
    this.num_q = num_q;
    this.num_ret = num_ret;
    this.num_rel = num_rel;
    this.num_rel_ret = num_rel_ret;
    this.map = map;
    this.gm_map = gm_map;
    this.Rprec = Rprec;
    this.bpref = bpref;
    this.recip_rank = recip_rank;
    this.iprec_at_recall_000 = iprec_at_recall_000;
    this.iprec_at_recall_010 = iprec_at_recall_010;
    this.iprec_at_recall_020 = iprec_at_recall_020;
    this.iprec_at_recall_030 = iprec_at_recall_030;
    this.iprec_at_recall_040 = iprec_at_recall_040;
    this.iprec_at_recall_050 = iprec_at_recall_050;
    this.iprec_at_recall_060 = iprec_at_recall_060;
    this.iprec_at_recall_070 = iprec_at_recall_070;
    this.iprec_at_recall_080 = iprec_at_recall_080;
    this.iprec_at_recall_090 = iprec_at_recall_090;
    this.iprec_at_recall_100 = iprec_at_recall_100;
    this.P_5 = P_5;
    this.P_10 = P_10;
    this.P_15 = P_15;
    this.P_20 = P_20;
    this.P_30 = P_30;
    this.P_100 = P_100;
    this.P_200 = P_200;
    this.P_500 = P_500;
    this.P_1000 = P_1000;
    this.original = original;
  }

  public String getRunid() {
    return runid;
  }

  public int getNum_q() {
    return num_q;
  }

  public int getNum_ret() {
    return num_ret;
  }

  public int getNum_rel() {
    return num_rel;
  }

  public int getNum_rel_ret() {
    return num_rel_ret;
  }

  public double getMap() {
    return map;
  }

  public double getGm_map() {
    return gm_map;
  }

  public double getRprec() {
    return Rprec;
  }

  public double getBpref() {
    return bpref;
  }

  public double getRecip_rank() {
    return recip_rank;
  }

  public double getIprec_at_recall_000() {
    return iprec_at_recall_000;
  }

  public double getIprec_at_recall_010() {
    return iprec_at_recall_010;
  }

  public double getIprec_at_recall_020() {
    return iprec_at_recall_020;
  }

  public double getIprec_at_recall_030() {
    return iprec_at_recall_030;
  }

  public double getIprec_at_recall_040() {
    return iprec_at_recall_040;
  }

  public double getIprec_at_recall_050() {
    return iprec_at_recall_050;
  }

  public double getIprec_at_recall_060() {
    return iprec_at_recall_060;
  }

  public double getIprec_at_recall_070() {
    return iprec_at_recall_070;
  }

  public double getIprec_at_recall_080() {
    return iprec_at_recall_080;
  }

  public double getIprec_at_recall_090() {
    return iprec_at_recall_090;
  }

  public double getIprec_at_recall_100() {
    return iprec_at_recall_100;
  }

  public double getP_5() {
    return P_5;
  }

  public double getP_10() {
    return P_10;
  }

  public double getP_15() {
    return P_15;
  }

  public double getP_20() {
    return P_20;
  }

  public double getP_30() {
    return P_30;
  }

  public double getP_100() {
    return P_100;
  }

  public double getP_200() {
    return P_200;
  }

  public double getP_500() {
    return P_500;
  }

  public double getP_1000() {
    return P_1000;
  }

  public boolean isOriginal() {
    return original;
  }

  @Override
  public String toString() {
    return "trecResult{" +
        "runid='" + runid + '\'' +
        ", num_q=" + num_q +
        ", num_ret=" + num_ret +
        ", num_rel=" + num_rel +
        ", num_rel_ret=" + num_rel_ret +
        ", map=" + map +
        ", gm_map=" + gm_map +
        ", Rprec=" + Rprec +
        ", bpref=" + bpref +
        ", recip_rank=" + recip_rank +
        ", iprec_at_recall_000=" + iprec_at_recall_000 +
        ", iprec_at_recall_010=" + iprec_at_recall_010 +
        ", iprec_at_recall_020=" + iprec_at_recall_020 +
        ", iprec_at_recall_030=" + iprec_at_recall_030 +
        ", iprec_at_recall_040=" + iprec_at_recall_040 +
        ", iprec_at_recall_050=" + iprec_at_recall_050 +
        ", iprec_at_recall_060=" + iprec_at_recall_060 +
        ", iprec_at_recall_070=" + iprec_at_recall_070 +
        ", iprec_at_recall_080=" + iprec_at_recall_080 +
        ", iprec_at_recall_090=" + iprec_at_recall_090 +
        ", iprec_at_recall_100=" + iprec_at_recall_100 +
        ", P_5=" + P_5 +
        ", P_10=" + P_10 +
        ", P_15=" + P_15 +
        ", P_20=" + P_20 +
        ", P_30=" + P_30 +
        ", P_100=" + P_100 +
        ", P_200=" + P_200 +
        ", P_500=" + P_500 +
        ", P_1000=" + P_1000 +
        ", original=" + original +
        '}';
  }
}
