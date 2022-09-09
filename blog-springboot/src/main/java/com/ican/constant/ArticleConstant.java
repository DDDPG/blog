package com.ican.constant;

/**
 * 文章常量
 *
 * @author ican
 */
@SuppressWarnings(value = "all")
public class ArticleConstant {
    public static final String MAPPING_TEMPLATE = "PUT /article\n" +
            "{\n" +
            "  \"settings\": {\n" +
            "    \"analysis\": {\n" +
            "      \"analyzer\": {\n" +
            "        \"text_anlyzer\": {\n" +
            "          \"tokenizer\": \"ik_max_word\",\n" +
            "          \"filter\": \"py\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"filter\": {\n" +
            "        \"py\": {\n" +
            "          \"type\": \"pinyin\",\n" +
            "          \"keep_full_pinyin\": false,\n" +
            "          \"keep_joined_full_pinyin\": true,\n" +
            "          \"keep_original\": true,\n" +
            "          \"limit_first_letter_length\": 16,\n" +
            "          \"remove_duplicated_term\": true,\n" +
            "          \"none_chinese_pinyin_tokenize\": false\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"all\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"text_anlyzer\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"articleTitle\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"text_anlyzer\",\n" +
            "        \"search_analyzer\": \"ik_smart\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"articleContent\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"text_anlyzer\",\n" +
            "        \"search_analyzer\": \"ik_smart\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"isDelete\": {\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"status\": {\n" +
            "        \"type\": \"integer\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
