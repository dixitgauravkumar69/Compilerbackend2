import sys
from tree_sitter import Language, Parser
import tree_sitter_java, tree_sitter_cpp, tree_sitter_python

# Load Grammars
JAVA_LANG = Language(tree_sitter_java.language())
CPP_LANG = Language(tree_sitter_cpp.language())
PY_LANG = Language(tree_sitter_python.language())
parser = Parser()

def get_node_text(node):
    return node.text.decode('utf8').lower()

def extract_func_name(node):
    if node.type in ['function_definition', 'method_declaration']:
        for i in range(node.child_count):
            child = node.child(i)
            if child.type == 'identifier': return child.text.decode('utf8')
            if child.type in ['function_declarator', 'method_declarator']: return extract_func_name(child)
    for i in range(node.child_count):
        res = extract_func_name(node.child(i))
        if res: return res
    return None

def analyze_logic(node, func_name):
    depth, rec, is_log, is_sqrt = 0, 0, False, False
    if node.type in ['for_statement', 'while_statement', 'for_in_clause']:
        depth = 1
        text = get_node_text(node)
        if ('*' in text and ('<' in text or '<=' in text)) or 'sqrt' in text: is_sqrt = True

    if node.type in ['call_expression', 'method_invocation'] and func_name:
        text = get_node_text(node)
        if func_name in text:
            rec = 1
            if any(op in text for op in ['/', '>>', '>>>']): is_log = True

    m_depth, t_rec, c_log, c_sqrt = 0, 0, False, False
    for i in range(node.child_count):
        d, r, l, s = analyze_logic(node.child(i), func_name)
        m_depth = max(m_depth, d)
        t_rec += r
        if l: c_log = True
        if s: c_sqrt = True
    return depth + m_depth, rec + t_rec, is_log or c_log, is_sqrt or c_sqrt

if __name__ == "__main__":
    # Command line args: python3 analyzer.py <file_path> <lang>
    file_path = sys.argv[1]
    lang = sys.argv[2].lower()

    with open(file_path, 'r') as f: code = f.read()

    lang_map = {'java': JAVA_LANG, 'cpp': CPP_LANG, 'python': PY_LANG}
    parser.set_language(lang_map.get(lang, PY_LANG))
    tree = parser.parse(bytes(code, "utf8"))

    name = extract_func_name(tree.root_node)
    d, r, log, sqrt = analyze_logic(tree.root_node, name)

    if r > 0: print("O(log n)" if log else (f"O({r}^n)" if r > 1 else "O(n)"))
    elif sqrt: print("O(sqrt(n))")
    elif d > 0: print(f"O(n^{d})" if d > 1 else "O(n)")
    else: print("O(1)")