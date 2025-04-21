package com.mengnnakk.service;

import java.time.LocalDate;

public interface SignService {
    public void sign(String userId);
    public boolean isSigned(String userId, LocalDate date);

    public long getSignCount(String userId, LocalDate date);

    public void printRank(int topN);


}
