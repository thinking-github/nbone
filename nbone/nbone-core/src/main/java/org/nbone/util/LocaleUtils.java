package org.nbone.util;

import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-12-20
 */
@SuppressWarnings("unused")
public class LocaleUtils {

    /**
     * 国家编码对应的全文本地化名称
     * jdk default AvailableLocales
     * <p>
     * 支持主要国家
     * {AE=阿拉伯联合酋长国, JO=约旦, SY=叙利亚, HR=克罗地亚, BE=比利时}
     *
     * @param displayLocale 本地化显示
     * @return
     */
    public static Map<String, String> getCountryMap(Locale displayLocale) {
        Locale[] localeList = Locale.getAvailableLocales();
        Map<String, String> countryMap = new LinkedHashMap<String, String>(localeList.length);
        for (Locale locale : localeList) {
            if (StringUtils.hasText(locale.getCountry())) {
                countryMap.put(locale.getCountry(), locale.getDisplayCountry(displayLocale));
            }
        }
        //巴基斯坦
        if(!countryMap.containsKey("PK")){
            Locale locale = new Locale("","PK");
            countryMap.put("PK",locale.getDisplayCountry(displayLocale));
        }
        return countryMap;
    }

    /**
     * 语言编码对应的全文本地化名称
     * jdk default AvailableLocales  格式 en zh
     * <p>
     * 支持主要语言
     * {ar=阿拉伯文, hr=克罗地亚文, fr=法文, es=西班牙文}
     *
     * @param displayLocale 本地化显示
     * @return
     */
    public static Map<String, String> getLanguageMap(Locale displayLocale) {
        Locale[] localeList = Locale.getAvailableLocales();
        Map<String, String> languageMap = new LinkedHashMap<String, String>(localeList.length);
        for (Locale locale : localeList) {
            if (StringUtils.hasText(locale.getLanguage())) {
                languageMap.put(locale.getLanguage(), locale.getDisplayLanguage(displayLocale));
            }
        }
        // zh_CN zh-HK zh-SG zh-TW
        languageMap.put(Locale.SIMPLIFIED_CHINESE.toLanguageTag(), Locale.SIMPLIFIED_CHINESE.getDisplayName(displayLocale));
        Locale zh_HK = Locale.forLanguageTag("zh-HK");
        Locale zh_SG = Locale.forLanguageTag("zh-SG");
        languageMap.put(zh_HK.toLanguageTag(), zh_HK.getDisplayName(displayLocale));
        languageMap.put(zh_SG.toLanguageTag(), zh_SG.getDisplayName(displayLocale));
        languageMap.put(Locale.TRADITIONAL_CHINESE.toLanguageTag(), Locale.TRADITIONAL_CHINESE.getDisplayName(displayLocale));

        return languageMap;
    }


    public static Map<String, String> getLanguageCountryMap(Locale displayLocale) {
        return getLanguageCountryMap(displayLocale, (String) null);
    }

    /**
     * 语言国家组合编码对应的全文本地化名称
     * jdk default AvailableLocales 含语言国家格式  zh_CN zh-HK zh-SG zh-TW
     * <p>
     * 支持主要语言
     * {ar-AE=阿拉伯文 (阿拉伯联合酋长国), ar-JO=阿拉伯文 (约旦), ar-SY=阿拉伯文 (叙利亚)}
     *
     * @param displayLocale 本地化显示
     * @param join          显示信息的语言码和国家码之间连接符号 可为空,默认为 中文(中国)
     * @return
     */
    public static Map<String, String> getLanguageCountryMap(Locale displayLocale, String join) {
        Locale[] localeList = Locale.getAvailableLocales();
        Map<String, String> languageMap = new LinkedHashMap<String, String>(localeList.length);
        for (Locale locale : localeList) {
            if (StringUtils.hasText(locale.getLanguage()) && StringUtils.hasText(locale.getCountry())) {
                String langCode = locale.toLanguageTag();
                String displayName;
                if (join == null || join.length() == 0) {
                    displayName = locale.getDisplayName(displayLocale);
                } else {
                    displayName = locale.getDisplayLanguage(displayLocale) + join + locale.getDisplayCountry(displayLocale);
                }
                languageMap.put(langCode, displayName);
            }
        }
        return languageMap;
    }


    /**
     * 获取某个国家使用的语言列表
     *   {hi-IN=印地文, en-IN=英文}
     *
     * @param country       国家代码
     * @param displayLocale 本地化显示
     * @return
     */
    public static Map<String, String> getLanguageCountryMap(String country, Locale displayLocale) {
        if (country == null) {
            throw new IllegalArgumentException("country/region code must not be null.");
        }
        Locale[] localeList = Locale.getAvailableLocales();
        Map<String, String> languageMap = new LinkedHashMap<String, String>();
        for (Locale locale : localeList) {
            if (StringUtils.hasText(locale.getLanguage()) && country.equals(locale.getCountry())) {
                String langCode = locale.toLanguageTag();
                String displayName = locale.getDisplayLanguage(displayLocale);
                languageMap.put(langCode, displayName);
            }
        }
        return languageMap;
    }

    /**
     * 获取使用某个语言的国家列表
     *
     * <pre>
     *  {en-US=美国, en-SG=新加坡, en-MT=马耳他, en-PH=菲律宾, en-NZ=新西兰,
     *   en-ZA=南非,en-AU=澳大利亚, en-IE=爱尔兰, en-CA=加拿大, en-IN=印度, en-GB=英国}
     *
     *   {zh-TW=台湾地区, zh-HK=香港, zh-SG=新加坡, zh-CN=中国}
     * </pre>
     *
     * @param language      语言代码
     * @param displayLocale 本地化显示
     * @return
     */
    public static Map<String, String> getLanguageCountryMap(Locale language, Locale displayLocale) {
        if (language == null) {
            throw new IllegalArgumentException("language code must not be null.");
        }
        Locale[] localeList = Locale.getAvailableLocales();
        Map<String, String> languageMap = new LinkedHashMap<String, String>();
        for (Locale locale : localeList) {
            if (language.getLanguage().equals(locale.getLanguage()) && StringUtils.hasText(locale.getCountry())) {
                String langCode = locale.toLanguageTag();
                String displayName = locale.getDisplayCountry(displayLocale);
                languageMap.put(langCode, displayName);
            }
        }
        return languageMap;
    }

    /**
     * 获取使用某个语言的国家列表
     * <pre>
     * {US=美国, SG=新加坡, MT=马耳他, PH=菲律宾, NZ=新西兰, ZA=南非, AU=澳大利亚, IE=爱尔兰, CA=加拿大, IN=印度, GB=英国}
     *
     * {TW=台湾地区, HK=香港, SG=新加坡, CN=中国}
     * </pre>
     *
     * @param language      语言代码
     * @param displayLocale 本地化显示
     * @return
     */
    public static Map<String, String> getCountryMap(Locale language, Locale displayLocale) {
        if (language == null) {
            throw new IllegalArgumentException("language code must not be null.");
        }
        Locale[] localeList = Locale.getAvailableLocales();
        Map<String, String> languageMap = new LinkedHashMap<String, String>();
        for (Locale locale : localeList) {
            if (language.getLanguage().equals(locale.getLanguage()) && StringUtils.hasText(locale.getCountry())) {
                String code = locale.getCountry();
                String displayName = locale.getDisplayCountry(displayLocale);
                languageMap.put(code, displayName);
            }
        }
        return languageMap;
    }


    /**
     * ISO Locales country 国家编码对应的全文本地化名称
     * <p>
     * 支持全部国家
     * {AD=安道尔, AE=阿拉伯联合酋长国, AF=阿富汗, AG=安提瓜和巴布达, AI=安圭拉, AL=阿尔巴尼亚}
     *
     * @param displayLocale 本地化显示
     * @return
     */
    public static Map<String, String> getISOCountryMap(Locale displayLocale) {
        String[] isoCountries = Locale.getISOCountries();
        Map<String, String> countryMap = new LinkedHashMap<String, String>(isoCountries.length);
        for (String countryCode : isoCountries) {
            Locale locale = Locale.forLanguageTag("en-" + countryCode);
            countryMap.put(countryCode, locale.getDisplayCountry(displayLocale));
        }
        return countryMap;
    }

    /**
     * ISO Locales language 语言编码对应的全文本地化名称
     * <p>
     * 支持全部语言
     * {aa=阿法文, ab=阿布哈西亚文, ae=阿维斯陀文, af=南非荷兰文, ak=库阿文, am=阿姆哈拉文, an=阿拉贡文, ar=阿拉伯文}
     *
     * @param displayLocale 本地化显示
     * @return
     */
    public static Map<String, String> getISOLanguageMap(Locale displayLocale) {
        String[] isoLanguages = Locale.getISOLanguages();
        Map<String, String> countryMap = new LinkedHashMap<String, String>(isoLanguages.length);
        for (String languageCode : isoLanguages) {
            Locale locale = Locale.forLanguageTag(languageCode);
            countryMap.put(languageCode, locale.getDisplayLanguage(displayLocale));
        }
        return countryMap;
    }

}
