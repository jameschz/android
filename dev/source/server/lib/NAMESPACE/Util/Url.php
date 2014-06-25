<?php
/**
 * NAMESPACE Util
 *
 * @category   NAMESPACE
 * @package    NAMESPACE_Util
 * @author     James.Huang <huangjuanshi@snda.com>
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    $Id$
 */

/**
 * @package NAMESPACE_Util
 */
class NAMESPACE_Util_Url
{
	static public function format ($url)
	{
		$url = parse_url($url);
		$url = $url['path'] . '?sid=' . session_id() . '&' . $url['query'];
		return $url;
	}
}