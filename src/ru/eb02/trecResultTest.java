package ru.eb02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class trecResultTest {
  String runid = "test";
  int num_q = 200;
  int num_ret = 199;
  int num_rel = 198;
  int num_rel_ret = 197;
  double map = 0.99;
  double gm_map = 0.98;
  double Rprec = 0.97;
  double bpref = 0.96;
  double recip_rank = 0.95;
  double iprec_at_recall_000 = 0.94;
  double iprec_at_recall_010 = 0.93;
  double iprec_at_recall_020 = 0.92;
  double iprec_at_recall_030 = 0.91;
  double iprec_at_recall_040 = 0.90;
  double iprec_at_recall_050 = 0.89;
  double iprec_at_recall_060 = 0.88;
  double iprec_at_recall_070 = 0.87;
  double iprec_at_recall_080 = 0.86;
  double iprec_at_recall_090 = 0.85;
  double iprec_at_recall_100 = 0.84;
  double P_5 = 0.83;
  double P_10 = 0.82;
  double P_15 = 0.81;
  double P_20 = 0.80;
  double P_30 = 0.79;
  double P_100 = 0.78;
  double P_200 = 0.77;
  double P_500 = 0.76;
  double P_1000 = 0.75;
  boolean original = true;

  @Test
  public void TestInit() {

    trecResult trTest = new trecResult(runid, num_q, num_ret, num_rel, num_rel_ret, map, gm_map, Rprec,
        bpref, recip_rank, iprec_at_recall_000, iprec_at_recall_010, iprec_at_recall_020,
        iprec_at_recall_030, iprec_at_recall_040, iprec_at_recall_050, iprec_at_recall_060,
        iprec_at_recall_070, iprec_at_recall_080, iprec_at_recall_090, iprec_at_recall_100,
        P_5, P_10, P_15, P_20, P_30, P_100, P_200, P_500, P_1000, original);

    // Simple tests to get re-acquainted with Junit.
    assertEquals(iprec_at_recall_050, trTest.getIprec_at_recall_050());
    assertEquals(iprec_at_recall_020, trTest.getIprec_at_recall_020());
    assertEquals(iprec_at_recall_090, trTest.getIprec_at_recall_090());
  }
}