package edu.mit.civic.mediacloud.test.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bericotech.clavin.gazetteer.CountryCode;

import edu.mit.civic.mediacloud.ParseManager;

/**
 * Print out the accuracy of our Aboutness algorithm against the hand-coded bake-off data.
 * Not a unit test, because we don't have a threshold at which this is "correct"
 * @author rahulb
 *
 */
public class HandCodedAboutnessCheck {

    private static final Logger logger = LoggerFactory.getLogger(HandCodedDisambiguationTest.class);

    private static double getAboutnessAccuracy(String filePath) throws Exception{
        int correct = 0;
        List<CodedArticle> articles = TestUtils.loadExamplesFromFile(filePath);
        for(CodedArticle article: articles){
            logger.info("Testing article "+article.mediacloudId+" (looking for "+article.handCodedPlaceName+" / "+article.handCodedCountryCode+")");
            List<CountryCode> primaryCountries = ParseManager.extractAndResolve(article.text).getUniqueCountries();
            if(article.isAboutHandCodedCountry(primaryCountries)) {
                correct++;
            } else {
                logger.error("Didn't find "+article.handCodedCountryCode+" in "+primaryCountries);
            }
        }
        return (double)correct/(double)articles.size();
    }

    public static void main(String [] args) throws Exception {
        double nytPct = getAboutnessAccuracy(TestUtils.NYT_JSON_PATH);
        double huffpoPct = getAboutnessAccuracy(TestUtils.HUFFPO_JSON_PATH);
        double bbcPct = getAboutnessAccuracy(TestUtils.BBC_JSON_PATH);
        logger.info("Aboutness gets NYT "+nytPct+" correct");
        logger.info("Aboutness gets Huff Po "+huffpoPct+" correct");
        logger.info("Aboutness gets BBC "+bbcPct+" correct");
        
        ParseManager.logStats();
    }

}
