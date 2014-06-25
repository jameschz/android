<?php
/**
 * Weibo Util
 *
 * @category   Weibo
 * @package    Weibo_Util
 * @author     James.Huang <huangjuanshi@snda.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

/**
 * @package Weibo_Util
 */
class Weibo_Util_Url
{
	static public function format ($url)
	{
		$url = parse_url($url);
		$url = $url['path'] . '?sid=' . session_id() . '&' . $url['query'];
		return $url;
	}
}