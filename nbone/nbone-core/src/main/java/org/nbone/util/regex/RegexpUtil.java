/*
 * Created on 2005-4-15
 *
 * Summary of regular-expression constructs										正则表达式结构简介：
 * Construct Matches
 *	Characters																	字符：
 *		x The character x															x   字符 x
 *		\\ The backslash character													\\  反斜杠
 *		\0n The character with octal value 0n (0 <= n <= 7)							\0n     十进制数 (0 <= n <= 7)
 *		\0nn The character with octal value 0nn (0 <= n <= 7)						\0nn    十进制数 0nn (0 <= n <= 7)
 *		\0mnn The character with octal value 0mnn (0 <= m <= 3, 0 <= n <= 7)		\0mnn   十进制数 0mnn (0 <= m <= 3, 0 <= n <= 7)
 *		\xhh The character with hexadecimal value 0xhh								\xhh    十六进制数 0xhh
 *		\\uhhhh The character with hexadecimal value 0xhhhh						\\uhhhh  十六进制数 0xhhhh
 *		\t The tab character ('\u0009')												\t  制表符 ('\u0009')
 *		\n The newline (line feed) character ('\u000A')								\n  换行符 ('\u000A')
 *		\r The carriage-return character ('\u000D')									\r  回车符 ('\u000D')
 *		\f The form-feed character ('\u000C')										\f  The form-feed character ('\u000C')
 *		\a The alert (bell) character ('\u0007')									\a  The alert (bell) character ('\u0007')
 *		\e The escape character ('\u001B')											\e  esc符号 ('\u001B')
 *		\cx The control character corresponding to x								\cx     x 对应的控制符
 *
 *	 Character classes															字符类
 *	 [abc] a, b, or c (simple class)												[abc]       a, b, 或 c (简单字符串)
 *	 [^abc] Any character except a, b, or c (negation)								[^abc]      除了 a, b, 或 c 之外的任意字符(否定)
 *	 [a-zA-Z] a through z or A through Z, inclusive (range)							[a-zA-Z]    从a 到 z 或 从A 到 Z（包括a,z,A,Z）(范围)
 *	 [a-d[m-p]] a through d, or m through p: [a-dm-p] (union)						[a-d[m-p]]  从a 到 d, 或 从m 到 p: [a-dm-p] (并集)
 *	 [a-z&&[def]] d, e, or f (intersection)											[a-z&&[def]]    d, e, 或 f (交集)
 *	 [a-z&&[^bc]] a through z, except for b and c: [ad-z] (subtraction)				[a-z&&[^bc]]    从a 到 z, 但 b 和 c 除外: [ad-z] (子集)
 *	 [a-z&&[^m-p]] a through z, and not m through p: [a-lq-z](subtraction)			[a-z&&[^m-p]]   从a 到 z, 不包括从 m 到 p: [a-lq-z](子集)
 *
 * 	Predefined character classes												预定义字符序列
 *	. Any character (may or may not match line terminators)							 .   任意字符 (也可能不包括行结束符)
 *	\d A digit: [0-9]																 \d  数字: [0-9]
 *	\D A non-digit: [^0-9]															 \D  非数字: [^0-9]
 *	\s A whitespace character: [ \t\n\x0B\f\r]										 \s  空字符: [ \t\n\x0B\f\r]
 *	\S A non-whitespace character: [^\s]											 \S  非空字符: [^\s]
 *	\w A word character: [a-zA-Z_0-9]												 \w  单字字符: [a-zA-Z_0-9]
 *	\W A non-word character: [^\w]													 \W  非单字字符: [^\w]
 *
 *	POSIX character classes (US-ASCII only)										  POSIX 字符类 (US-ASCII only)
 *	\p{Lower} A lower-case alphabetic character: [a-z]								\p{Lower}   小写字母字符: [a-z]
 *	\p{Upper} An upper-case alphabetic character:[A-Z]								\p{Upper}   大写字母字符:[A-Z]
 *	\p{ASCII} All ASCII:[\x00-\x7F]													\p{ASCII}   所有 ASCII:[\x00-\x7F]
 *	\p{Alpha} An alphabetic character:[\p{Lower}\p{Upper}]							\p{Alpha}   单个字母字符:[\p{Lower}\p{Upper}]
 *	\p{Digit} A decimal digit: [0-9]												\p{Digit}   十进制数: [0-9]
 *	\p{Alnum} An alphanumeric character:[\p{Alpha}\p{Digit}]						\p{Alnum}   单个字符:[\p{Alpha}\p{Digit}]
 *	\p{Punct} Punctuation: One of !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~					\p{Punct}   标点符号: 包括 !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
 *	\p{Graph} A visible character: [\p{Alnum}\p{Punct}]								\p{Graph}   可视字符: [\p{Alnum}\p{Punct}]
 *	\p{Print} A printable character: [\p{Graph}]									\p{Print}   可打印字符: [\p{Graph}]
 *	\p{Blank} A space or a tab: [ \t]												\p{Blank}   空格或制表符: [ \t]
 *	\p{Cntrl} A control character: [\x00-\x1F\x7F]									\p{Cntrl}   控制字符: [\x00-\x1F\x7F]
 *	\p{XDigit} A hexadecimal digit: [0-9a-fA-F]										\p{XDigit}  十六进制数: [0-9a-fA-F]
 *	\p{Space} A whitespace character: [ \t\n\x0B\f\r]								\p{Space}   空字符: [ \t\n\x0B\f\r]
 *
 *	Classes for Unicode blocks and categories									  Unicode 字符类
 *	\p{InGreek} A character in the Greek block (simple block)						\p{InGreek}     希腊语种的字符 (simple block)
 *	\p{Lu} An uppercase letter (simple category)									\p{Lu}      大写字母 (simple category)
 *	\p{Sc} A currency symbol														\p{Sc}      货币符号
 *	\P{InGreek} Any character except one in the Greek block (negation)				\P{InGreek}     除希腊语种字符外的任意字符 (negation)
 *	[\p{L}&&[^\p{Lu}]]  Any letter except an uppercase letter (subtraction)			[\p{L}&&[^\p{Lu}]]  除大写字母外的任意字符 (subtraction)
 *
 *	Boundary matchers															 边界匹配器
 *	^ The beginning of a line														^   一行的开始
 *	$ The end of a line																$  一行的结束
 *	\b A word boundary																\b  单词边界
 *	\B A non-word boundary															\B  非单词边界
 *	\A The beginning of the input													\A  输入的开始
 *	\G The end of the previous match												\G  当前匹配的结束
 *	\Z The end of the input but for the final terminator, if any					\Z  The end of the input but for the final terminator, if any
 *	\z The end of the input															\z  输入的结束
 *
 *	Greedy quantifiers											Greedy quantifiers 贪婪匹配量词（Greedy quantifiers ）（不知道翻译的对不对）
 *	X? X, once or not at all									   	X?          X不出现或出现一次  (特殊字符"?"与{0,1}是相等的)
 *	X* X, zero or more times									   	X*          X不出现或出现多次  (特殊字符"*"与{0,}是相等的)
 *	X+ X, one or more times								   	X+          X至少出现一次      (特殊字符"+"与 {1,}是相等的)
 *	X{n} X, exactly n times														   	X{n}        X出现n次
 *	X{n,} X, at least n times													   	X{n,}       X至少出现n次
 *	X{n,m} X, at least n but not more than m times								   	X{n,m}      X至少出现n次，但不会超过m次
 *
 *	Reluctant quantifiers														   Reluctant quantifiers
 *	X?? X, once or not at all													   	X??         X, 不出现或出现一次
 *	X*? X, zero or more times													   	X*?         X, 不出现或出现多次
 *	X+? X, one or more times													   	X+?         X, 至少出现一次
 *	X{n}? X, exactly n times													   	X{n}?       X, 出现n次
 *	X{n,}? X, at least n times													   	X{n,}?      X, 至少出现n次
 *	X{n,m}? X, at least n but not more than m times								   	X{n,m}?     X, 至少出现n次，但不会超过m次
 *
 *	Possessive quantifiers														   	Possessive quantifiers
 *	X?+ X, once or not at all													   	X?+     X, 不出现或出现一次
 *	X*+ X, zero or more times													   	X*+         X, 不出现或出现多次
 *	X++ X, one or more times													   	X++         X, 至少出现一次
 *	X{n}+ X, exactly n times													   	X{n}+       X, 出现n次
 *	X{n,}+ X, at least n times													   	X{n,}+      X, 至少出现n次
 *	X{n,m}+ X, at least n but not more than m times								   	X{n,m}+     X, 至少出现n次，但不会超过m次
 *
 *  Logical operators                                                             逻辑运算符
 *	XY X followed by Y                                                               XY  Y跟在X后面
 *	X|Y Either X or Y                                                                	X|Y     X 或 Y
 *	(X) X, as a capturing group                                                      	(X)     X, as a capturing group
 *
 *	Back references                                                                  	反向引用
 *	\n Whatever the nth capturing group matched                                      	\n  Whatever the nth capturing group matched
 *
 *	Quotation                                                                        Quotation
 *	\ Nothing, but quotes the following character                                    	\   引用后面的字符
 *	\Q Nothing, but quotes all characters until \E                                   	\Q  引用所有的字符直到 \E 出现
 *	\E Nothing, but ends quoting started by \Q                                       	\E  结束以 \Q 开始的引用
 *
 *	Special constructs (non-capturing)                                               Special constructs (non-capturing)
 *	(?:X) X, as a non-capturing group                                                	(?:X)           X, as a non-capturing group
 *	(?idmsux-idmsux)  Nothing, but turns match flags on - off                        	(?idmsux-idmsux)    匹配标志开关
 *	(?idmsux-idmsux:X)   X, as a non-capturing group with the given flags on - off   	(?idmsux-idmsux:X)      X, as a non-capturing group with the given flags on
 *	(?=X) X, via zero-width positive lookahead	- off
 *	(?!X) X, via zero-width negative lookahead                                       	(?=X)           X, via zero-width positive lookahead
 *	(?<=X) X, via zero-width positive lookbehind                                     	(?!X)           X, via zero-width negative lookahead
 *	(?<!X) X, via zero-width negative lookbehind                                     	(?<=X)          X, via zero-width positive lookbehind
 *	(?>X) X, as an independent, non-capturing group                                  	(?<!X)          X, via zero-width negative lookbehind
 *	(?>X) X, as an independent, non-capturing group
 *
 *	Backslashes, escapes, and quoting
 *		反斜杠字符('\')用来转义，就像上面的表中定义的那样，如果不这样做的话可能会产生
 *		歧义。因此，表达式\\匹配
 *		单个反斜杠，表达式\{匹配单个左花括号。
 *		如果把反斜杠放在没有定义转移构造的任何字母符号前面都会发生错误，这些将被保留
 *		到以后的正则表达式中扩展。反斜杠可以放在任何
 *		非字母符号前面，即使它没有定义转义构造也不会发生错误。
 *		在java语言规范中指出，在java代码中自符串中的反斜杠是必要的，不管用于Unicode转
 *		义，还是用于普通的字符转义。因此，
 *		为了保持正则表达式的完整性，在java字符串中要写两个反斜杠。例如，在正则表达式
 *		中字符'\b'代表退格，'\\b'则代表单词边界。'\(hello\)'是无效的，并且会产生编译
 *		时错误，你必须用
 *		'\\(hello\\)'来匹配(hello)。
 *
 *	Character Classes
 *
 *	     字符类可以出现在其他字符类内部，并且可以由并操作符和与操作符(&&)组成。并集操
 *		作结果是，其中的任意字符，肯定在至少其中操作数中至少出现过一次。
 *		交集的结果包括各个操作数中同时出现的任意字符。
 *
 *		字符类操作符的优先级如下：（从高到低）
 *			1     文字转义      \x
 *			2     集合      [...]
 *			3     范围      a-z
 *			4     并集      [a-e][i-u]
 *			5     交集      [a-z&&[aeiou]]
 *
 *			请注意各个字符类的有效字符集。例如，在字符类中，正则表达式.失去了它的特别含义
 *			，而-变成了元字符的范围指示。
 *
 *		Line terminators
 *
 *			行结束符是一个或两个字符序列，用来标识输入字符序列的一行的结束。下列都被认为
 *			是行结束符：
 *
 *			换行符      ('\n'),
 *			回车换行符  ("\r\n"),
 *			回车符      ('\r'),
 *			下一行      ('\u0085'),
 *			行分隔符    ('\u2028'), 或
 *			段分隔符    ('\u2029).
 *
 *			如果激活了 UNIX_LINES 模式，唯一的行结束符就是换行符。
 *			除非你指定了 DOTALL 标志，否则正则表达式.匹配任何字符，只有行结束符除外。
 *			确省情况时，在整个输入队列中，正则表达式^和$忽略行结束符，只匹配开始和结束。
 *			如果激活了 MULTILINE 模式，则^匹配输入的开始和所有行结束符之后，除了整个输入
 *			的结束。
 *			在MULTILINE 模式下，$匹配所有行结束符之前，和整个输入的结束。
 *
 *		Groups and capturing
 *
 *			分组捕获通过从左到右的顺序，根据括号的数量类排序。例如，在表达式((A)(B(C)))中
 *			，有四个组：
 *			1     ((A)(B(C)))
 *			2     (A)
 *			3     (B(C))
 *			4     (C)
 *
 *			0组代表整个表达式。
 *			分组捕获之所以如此命名，是因为在匹配过程中，输入序列的每一个与分组匹配的子序
 *			列都会被保存起来。通过向后引用，被捕获的子序列可以在后面的表达式中被再次使用
 *			。
 *			而且，在匹配操作结束以后还可以通过匹配器重新找到。
 *			与一个分组关联的被捕获到的输入通常是被保存的最近与这个分组相匹配的队列的子队
 *			列。如果一个分组被第二次求值，即使失败，它的上一次被捕获的值也会被保存起来。
 *			例如，
 *			表达式(a(b)?)+匹配"aba"，"b"设为子分组。在开始匹配的时候，以前被捕获的输入都
 *			将被清除。
 *			以(?开始的分组是完全的，无需捕获的分组不会捕获任何文本，也不会计算分组总数。
 *
 *		Unicode support
 *
 *			Unicode Technical Report #18: Unicode Regular Expression Guidelines通过轻微的语法改变实现了更深层次的支持。
 *			在java代码中，像\u2014 这样的转义序列，java语言规范中？3.3提供了处理方法
 *			。
 *			为了便于使用从文件或键盘读取的unicode转义字符，正则表达式解析器也直接实现了这
 *			种转移。因此，字符串"\u2014"与"\\u2014"虽然不相等，但是编译进同一种模式，可以
 *			匹配
 *			十六进制数0x2014。
 *
 *			在Perl中，unicode块和分类被写入\p,\P。如果输入有prop属性，\p{prop}将会匹配，
 *			而\P{prop}将不会匹配。块通过前缀In指定，作为在nMongolian之中。
 *			分类通过任意的前缀Is指定： \p{L} 和 \p{IsL} 都引用 Unicode 字母。块和分类可以
 *			被使用在字符类的内部或外部。
 *
 *			The Unicode Standard, Version 3.0指出了支持的块和分类。块的名字在第14章和 Unicode Character
 *			Database中的 Blocks-3.txt 文件定义，
 *			但空格被剔除了。例如Basic Latin"变成了  "BasicLatin"。分类的名字被定义在88页
 *			，表4-5。
 *
 */
package org.nbone.util.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
* 目前支持25种正则表达式
* &gt;/pre&lt;
            1. 匹配图象 
            2. 匹配email地址 
            3. 匹配匹配并提取url 
			4. 匹配并提取http 
			5. 匹配日期 
			6. 匹配电话 
			7. 匹配身份证 
 			8. 匹配邮编代码
			9. 不包括特殊字符的匹配 (字符串中不包括符号 数学次方号^ 单引号' 双引号" 分号; 逗号, 帽号:数学减号- 右尖括号> 左尖括号<  反斜杠\ 即空格,制表符,回车符等
			10.匹配非负整数（正整数 + 0)
			11.匹配不包括零的非负整数（正整数 > 0)
            12.匹配正整数
            13.匹配非正整数（负整数 + 0）
            14.匹配负整数
            15.匹配整数
            16.匹配非负浮点数（正浮点数 + 0）
            17.匹配正浮点数
            18.匹配非正浮点数（负浮点数 + 0）
            19.匹配负浮点数
            20.匹配浮点数
            21.匹配由26个英文字母组成的字符串
            22.匹配由26个英文字母的大写组成的字符串
            23.匹配由26个英文字母的小写组成的字符串
            24.匹配由数字和26个英文字母组成的字符串
            25.匹配由数字、26个英文字母或者下划线组成的字符串
 *&gt;/pre&lt;
 */
public final class RegexpUtil{

	/**
	 * 匹配图象 <br>
	 *
	 * 格式: /相对路径/文件名.后缀 (后缀为gif,dmp,png)
	 * 匹配: 如/forum/head_icon/admini2005111_ff.gif 或 admini2005111.dmp<br>
	 * 不匹配: 如c:/admins4512.gif
	 */
	public static final String ICON_REGEXP = "^(/{0,1}\\w){1,}\\.(gif|dmp|png|jpg)$|^\\w{1,}\\.(gif|dmp|png|jpg)$";

	/**
	 * 匹配email地址 <br>
	 * 
	 * 格式: XXX@XXX.XXX.XX
	 * 匹配: foo@bar.com 或 foobar@foobar.com.au <br>
	 * 不匹配: foo@bar 或 $$$@bar.com
	 */
	public static final String EMAIL_REGEXP = "(?:\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3}$)";

	/**
	 * 匹配匹配并提取url <br>
	 *
	 * 格式: XXXX://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX
	 * 匹配 : http://www.suncer.com 或news://www<br>
	 * 提取(MatchResult matchResult=matcher.getMatch()):
	 *  			matchResult.group(0)= http://www.suncer.com:8080/index.html?login=true
	 *              matchResult.group(1) = http
	 *              matchResult.group(2) = www.suncer.com
	 *              matchResult.group(3) = :8080
	 *              matchResult.group(4) = /index.html?login=true
	 * 不匹配: c:\window
	 *
	 */
	public static final String URL_REGEXP = "(\\w+)://([^/:]+)(:\\d*)?([^#\\s]*)";

	/**
	 * 匹配并提取http <br>
	 *
	 * 格式: http://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX 或 ftp://XXX.XXX.XXX 或 https://XXX
	 *
	 * 匹配 : http://www.suncer.com:8080/index.html?login=true<br>
	 *
	 * 提取(MatchResult matchResult=matcher.getMatch()):
	 *  			matchResult.group(0)= http://www.suncer.com:8080/index.html?login=true
	 *              matchResult.group(1) = http
	 *              matchResult.group(2) = www.suncer.com
	 *              matchResult.group(3) = :8080
	 *              matchResult.group(4) = /index.html?login=true
	 *
	 * 不匹配: news://www
	 *
	 */
	public static final String HTTP_REGEXP = "(http|https|ftp)://([^/:]+)(:\\d*)?([^#\\s]*)";

	/**
	 * 匹配日期 <br>
	 *
	 * 格式(首位不为0): XXXX-XX-XX 或 XXXX XX XX 或 XXXX-X-X <br>
	 *
	 * 范围:1900--2099 <br>
	 *
	 * 匹配 : 2005-04-04 <br>
	 *
	 * 不匹配: 01-01-01
	 *
	 */
	// 匹配日期
	public static final String DATE_REGEXP = "^((((19){1}|(20){1})d{2})|d{2})[-\\s]{1}[01]{1}d{1}[-\\s]{1}[0-3]{1}d{1}$";

	/**
	 * 匹配电话 <br>
	 *
	 * 格式为: 0XXX-XXXXXX(10-13位首位必须为0) 或0XXX XXXXXXX(10-13位首位必须为0) 或 <br>
	 * (0XXX)XXXXXXXX(11-14位首位必须为0) 或 XXXXXXXX(6-8位首位不为0) 或
	 * XXXXXXXXXXX(11位首位不为0) <br>
	 *
	 * 匹配 : 0371-123456 或 (0371)1234567 或 (0371)12345678 或 010-123456 或
	 * 010-12345678 或 12345678912 <br>
	 *
	 * 不匹配: 1111-134355 或 0123456789
	 *
	 */
	public static final String PHONE_REGEXP = "^(?:0[0-9]{2,3}[-\\s]{1}|\\(0[0-9]{2,4}\\))[0-9]{6,8}$|^[1-9]{1}[0-9]{5,7}$|^[1-9]{1}[0-9]{10}$";

	/**
	 * 匹配身份证 <br>
	 *
	 * 格式为: XXXXXXXXXX(10位) 或 XXXXXXXXXXXXX(13位) 或 XXXXXXXXXXXXXXX(15位) 或
	 * XXXXXXXXXXXXXXXXXX(18位) <br>
	 *
	 * 匹配 : 0123456789123 <br>
	 *
	 * 不匹配: 0123456
	 *
	 */
	public static final String ID_CARD_REGEXP = "^\\d{10}|\\d{13}|\\d{15}|\\d{18}$";

	/**
	 * 匹配邮编代码 <br>
	 *
	 * 格式为: XXXXXX(6位) <br>
	 *
	 * 匹配 : 012345 <br>
	 *
	 * 不匹配: 0123456
	 *
	 */
	// 匹配邮编代码
	public static final String ZIP_REGEXP = "^[0-9]{6}$";


	/**
	 * 不包括特殊字符的匹配 (字符串中不包括符号 数学次方号^ 单引号' 双引号" 分号; 逗号, 帽号: 数学减号- 右尖括号> 左尖括号<  反斜杠\ 即空格,制表符,回车符等 )<br>
	 *
	 * 格式为: x 或 一个一上的字符 <br>
	 *
	 * 匹配 : 012345 <br>
	 *
	 * 不匹配: 0123456
	 *
	 */
	// 匹配邮编代码
	public static final String NON_SPECIAL_CHAR_REGEXP = "^[^'\"\\;,:-<>\\s].+$";


	/**
	 * 匹配非负整数（正整数 + 0)
	 */
	public static final String NON_NEGATIVE_INTEGERS_REGEXP = "^\\d+$";

	/**
	 * 匹配不包括零的非负整数（正整数 > 0)
	 */
	public static final String NON_ZERO_NEGATIVE_INTEGERS_REGEXP = "^[1-9]+\\d*$";

	/**
	 *
	 * 匹配正整数
	 *
	 */
	public static final String POSITIVE_INTEGER_REGEXP = "^[0-9]*[1-9][0-9]*$";

	/**
	 *
	 * 匹配非正整数（负整数 + 0）
	 *
	 */
	public static final String NON_POSITIVE_INTEGERS_REGEXP = "^((-\\d+)|(0+))$";

	/**
	 *
	 * 匹配负整数
	 *
	 */
	public static final String NEGATIVE_INTEGERS_REGEXP = "^-[0-9]*[1-9][0-9]*$";

	/**
	 *
	 * 匹配整数
	 *
	 */
	public static final String INTEGER_REGEXP = "^-?\\d+$";

	/**
	 *
	 * 匹配非负浮点数（正浮点数 + 0）
	 *
	 */
	public static final String NON_NEGATIVE_RATIONAL_NUMBERS_REGEXP = "^\\d+(\\.\\d+)?$";

	/**
	 *
	 * 匹配正浮点数
	 *
	 */
	public static final String POSITIVE_RATIONAL_NUMBERS_REGEXP = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";

	/**
	 *
	 * 匹配非正浮点数（负浮点数 + 0）
	 *
	 */
	public static final String NON_POSITIVE_RATIONAL_NUMBERS_REGEXP = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";

	/**
	 *
	 * 匹配负浮点数
	 *
	 */
	public static final String NEGATIVE_RATIONAL_NUMBERS_REGEXP = "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$";

	/**
	 *
	 * 匹配浮点数
	 *
	 */
	public static final String RATIONAL_NUMBERS_REGEXP = "^(-?\\d+)(\\.\\d+)?$";

	/**
	 *
	 * 匹配由26个英文字母组成的字符串
	 *
	 */
	public static final String LETTER_REGEXP = "^[A-Za-z]+$";

	/**
	 *
	 * 匹配由26个英文字母的大写组成的字符串
	 *
	 */
	public static final String UPWARD_LETTER_REGEXP = "^[A-Z]+$";

	/**
	 *
	 * 匹配由26个英文字母的小写组成的字符串
	 *
	 */
	public static final String LOWER_LETTER_REGEXP = "^[a-z]+$";

	/**
	 *
	 * 匹配由数字和26个英文字母组成的字符串
	 *
	 */
	public static final String LETTER_NUMBER_REGEXP = "^[A-Za-z0-9]+$";

	/**
	 *
	 * 匹配由数字、26个英文字母或者下划线组成的字符串
	 *
	 */
	public static final String LETTER_NUMBER_UNDERLINE_REGEXP = "^\\w+$";
	
	/**
	 * 创建匹配模式
	 * 
	 * @param regex 正则表达式
	 * @param isCaseSensitive 是否区分大小写
	 * @return 匹配模式
	 */
	public static Pattern getPattern(String regex, boolean isCaseSensitive) {
		if (isCaseSensitive) {
			return Pattern.compile(regex);
		}
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE);
	}
	
	/**
	 * 获取匹配结果
	 * 
	 * @param regex 正则表达式
	 * @param isCaseSensitive 是否区分大小写
	 * @param toMatch 字符序列
	 * @return 匹配结果
	 */
	public static Matcher getMatche(String regex, boolean isCaseSensitive, String toMatch) {
		Pattern pattern = getPattern(regex, isCaseSensitive);
		return pattern.matcher(toMatch);
	}
	
	/**
	 * 获取所有匹配值
	 * 
	 * @param regex 正则表达式
	 * @param isCaseSensitive 是否区分大小写
	 * @param toMatch 字符序列
	 * @return 匹配结果
	 */
	public static String[] getMatches(String regex, boolean isCaseSensitive, String toMatch) {
		Matcher matcher = getMatche(regex, isCaseSensitive, toMatch);
		List<String> result = new ArrayList<String>();
		while (matcher.find()) {
			result.add(matcher.group());
		}
		return result.toArray(new String[0]);
	}
	
	/**
	 * 正则表达式匹配字符序列
	 * 
	 * @param regex 正则表达式
	 * @param isCaseSensitive 是否区分大小写
	 * @param toMatch 字符序列
	 * @return 匹配结果:true=匹配成功 false=匹配失败
	 */
	public static boolean isMatche(String regex, boolean isCaseSensitive, String toMatch) {
		return getMatche(regex, isCaseSensitive, toMatch).matches();
	}
}