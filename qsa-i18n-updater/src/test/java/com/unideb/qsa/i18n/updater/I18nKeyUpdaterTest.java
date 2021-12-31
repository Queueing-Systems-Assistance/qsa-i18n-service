package com.unideb.qsa.i18n.updater;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.domain.exception.QSABatchUpdateException;

/**
 * Unit tests for {@link I18nKeyUpdater}.
 */
public class I18nKeyUpdaterTest {

    private static final List<DynamoDBMapper.FailedBatch> NO_FAILED_BATCH = List.of();
    private static final List<I18nElement> I18N_ELEMENT_LIST = List.of(new I18nElement("simple.element"));
    private static final List<DynamoDBMapper.FailedBatch> FAILED_BATCHES = List.of(new DynamoDBMapper.FailedBatch());

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private I18nKeyUpdater i18nKeyUpdater;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        i18nKeyUpdater = new I18nKeyUpdater(dynamoDBMapper);
    }

    @Test
    public void update() {
        // GIVEN
        given(dynamoDBMapper.batchSave(I18N_ELEMENT_LIST)).willReturn(NO_FAILED_BATCH);
        // WHEN
        i18nKeyUpdater.update(I18N_ELEMENT_LIST);
        // THEN
        verify(dynamoDBMapper).batchSave(I18N_ELEMENT_LIST);
    }

    @Test(expectedExceptions = QSABatchUpdateException.class)
    public void updateShouldThrowExceptionWhenBatchFails() {
        // GIVEN
        given(dynamoDBMapper.batchSave(I18N_ELEMENT_LIST)).willReturn(FAILED_BATCHES);
        // WHEN
        i18nKeyUpdater.update(I18N_ELEMENT_LIST);
        // THEN
    }
}
