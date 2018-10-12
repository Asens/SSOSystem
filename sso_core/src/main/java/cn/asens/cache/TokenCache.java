package cn.asens.cache;

/**
 * @author Asens
 */

public interface TokenCache {

    /**
     * 判断是否存在失效的token
     * @param key
     * @return
     */
    boolean containsInvalidKey(String key);

    /**
     * 添加失效的缓存
     * @param key
     * @return
     */
    void addInvalidKey(String key);

    /**
     * 删除失效的缓存
     * @param key
     * @return
     */
    void removeInvalidKey(String key);
}
